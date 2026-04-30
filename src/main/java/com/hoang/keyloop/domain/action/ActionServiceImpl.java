package com.hoang.keyloop.domain.action;

import com.hoang.keyloop.controller.dto.ActionRequest;
import com.hoang.keyloop.controller.dto.ActionResponse;
import com.hoang.keyloop.controller.dto.ActionUpdateRequest;
import com.hoang.keyloop.domain.exception.NotAgingVehicleException;
import com.hoang.keyloop.domain.vehicle.Vehicle;
import com.hoang.keyloop.domain.vehicle.VehicleRepository;
import com.hoang.keyloop.domain.vehicle.VehicleService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleService vehicleService;

    public ActionServiceImpl(ActionRepository actionRepository,
                             VehicleRepository vehicleRepository,
                             VehicleService vehicleService) {
        this.actionRepository = actionRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
    }

    private Vehicle getAndValidateAgingVehicle(UUID vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (vehicle.getInventoryReceiptDate() == null ||
                !vehicle.getInventoryReceiptDate().isBefore(LocalDate.now().minusDays(90))) {
            throw new NotAgingVehicleException("Vehicle is not an aging vehicle");
        }
        return vehicle;
    }

    private ActionResponse mapToResponse(Action action) {
        return new ActionResponse(
                action.getId(),
                action.getVehicle().getId(),
                action.getStatus(),
                action.getType(),
                action.getNote(),
                action.getCreatedAt(),
                action.getUpdatedAt(),
                action.getCreatedBy(),
                action.getUpdatedBy(),
                action.getVersion());
    }

    @Override
    public ActionResponse createAction(UUID vehicleId, ActionRequest request, String managerId) {
        Vehicle vehicle = getAndValidateAgingVehicle(vehicleId);

        Action action = new Action();
        action.setVehicle(vehicle);
        action.setStatus(request.getStatus());
        action.setType(request.getType());
        action.setNote(request.getNote());
        action.setCreatedBy(managerId);

        Action savedAction = actionRepository.save(action);
        vehicleService.invalidateAllCaches();
        return mapToResponse(savedAction);
    }

    @Override
    public ActionResponse updateAction(UUID vehicleId, UUID actionId, ActionUpdateRequest request, String managerId) {
        getAndValidateAgingVehicle(vehicleId);

        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new IllegalArgumentException("Action not found"));

        if (!action.getVehicle().getId().equals(vehicleId)) {
            throw new IllegalArgumentException("Action does not belong to the specified vehicle");
        }

        if (!action.getVersion().equals(request.getVersion())) {
            throw new OptimisticLockingFailureException("Concurrent modification detected. Please refresh and try again.");
        }

        action.setNote(request.getNote());
        action.setUpdatedBy(managerId);

        Action updatedAction = actionRepository.save(action);
        vehicleService.invalidateAllCaches();
        return mapToResponse(updatedAction);
    }

    @Override
    public void deleteAction(UUID vehicleId, UUID actionId) {
        getAndValidateAgingVehicle(vehicleId);

        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new IllegalArgumentException("Action not found"));

        if (!action.getVehicle().getId().equals(vehicleId)) {
            throw new IllegalArgumentException("Action does not belong to the specified vehicle");
        }

        actionRepository.delete(action);
        vehicleService.invalidateAllCaches();
    }

    @Override
    public List<ActionResponse> getActions(UUID vehicleId) {
        getAndValidateAgingVehicle(vehicleId);

        List<Action> actions = actionRepository.findByVehicleId(vehicleId);
        return actions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
