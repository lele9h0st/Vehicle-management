package com.hoang.keyloop.domain.action;

import com.hoang.keyloop.controller.dto.ActionRequest;
import com.hoang.keyloop.controller.dto.ActionResponse;
import com.hoang.keyloop.controller.dto.ActionUpdateRequest;
import com.hoang.keyloop.domain.exception.NotAgingVehicleException;
import com.hoang.keyloop.domain.vehicle.Vehicle;
import com.hoang.keyloop.domain.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private ActionServiceImpl actionService;

    private UUID vehicleId;
    private Vehicle agingVehicle;
    private Vehicle newVehicle;
    private ActionRequest request;
    private ActionUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        
        agingVehicle = new Vehicle();
        agingVehicle.setId(vehicleId);
        // Older than 90 days
        agingVehicle.setInventoryReceiptDate(LocalDate.now().minusDays(91));
        
        newVehicle = new Vehicle();
        newVehicle.setId(vehicleId);
        // Less than 90 days
        newVehicle.setInventoryReceiptDate(LocalDate.now().minusDays(30));

        request = new ActionRequest("PROPOSED", "DISCOUNT", "Note");
        updateRequest = new ActionUpdateRequest("Updated Note", 1L);
    }

    @Test
    void createAction_WithAgingVehicle_ShouldSucceed() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(agingVehicle));
        
        Action savedAction = new Action();
        savedAction.setId(UUID.randomUUID());
        savedAction.setVehicle(agingVehicle);
        savedAction.setStatus(request.getStatus());
        savedAction.setType(request.getType());
        savedAction.setNote(request.getNote());
        savedAction.setCreatedBy("manager1");
        
        when(actionRepository.save(any(Action.class))).thenReturn(savedAction);

        ActionResponse response = actionService.createAction(vehicleId, request, "manager1");

        assertNotNull(response);
        assertEquals("PROPOSED", response.getStatus());
        verify(actionRepository).save(any(Action.class));
    }

    @Test
    void createAction_WithNonAgingVehicle_ShouldThrowException() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(newVehicle));

        assertThrows(NotAgingVehicleException.class, () -> 
            actionService.createAction(vehicleId, request, "manager1")
        );

        verify(actionRepository, never()).save(any(Action.class));
    }

    @Test
    void updateAction_WithNonAgingVehicle_ShouldThrowException() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(newVehicle));

        assertThrows(NotAgingVehicleException.class, () -> 
            actionService.updateAction(vehicleId, UUID.randomUUID(), updateRequest, "manager1")
        );
    }

    @Test
    void getActions_WithNonAgingVehicle_ShouldThrowException() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(newVehicle));

        assertThrows(NotAgingVehicleException.class, () -> 
            actionService.getActions(vehicleId)
        );
    }

    @Test
    void deleteAction_WithNonAgingVehicle_ShouldThrowException() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(newVehicle));

        assertThrows(NotAgingVehicleException.class, () -> 
            actionService.deleteAction(vehicleId, UUID.randomUUID())
        );
    }
}
