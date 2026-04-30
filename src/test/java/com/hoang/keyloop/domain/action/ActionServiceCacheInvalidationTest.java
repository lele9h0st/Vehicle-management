package com.hoang.keyloop.domain.action;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hoang.keyloop.controller.dto.ActionRequest;
import com.hoang.keyloop.controller.dto.ActionResponse;
import com.hoang.keyloop.controller.dto.ActionUpdateRequest;
import com.hoang.keyloop.domain.exception.NotAgingVehicleException;
import com.hoang.keyloop.domain.vehicle.Vehicle;
import com.hoang.keyloop.domain.vehicle.VehicleRepository;
import com.hoang.keyloop.domain.vehicle.VehicleService;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests focused on the cache invalidation hooks added to ActionServiceImpl.
 * Each mutating operation (create / update / delete) must call
 * vehicleService.invalidateAllCaches() after a successful write.
 */
@ExtendWith(MockitoExtension.class)
class ActionServiceCacheInvalidationTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private ActionServiceImpl actionService;

    private UUID vehicleId;
    private UUID actionId;
    private Vehicle agingVehicle;
    private Vehicle newVehicle;
    private ActionRequest createRequest;
    private ActionUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        actionId = UUID.randomUUID();

        agingVehicle = new Vehicle();
        agingVehicle.setId(vehicleId);
        agingVehicle.setInventoryReceiptDate(LocalDate.now().minusDays(91));

        newVehicle = new Vehicle();
        newVehicle.setId(vehicleId);
        newVehicle.setInventoryReceiptDate(LocalDate.now().minusDays(30));

        createRequest = new ActionRequest("PROPOSED", "DISCOUNT", "Note");
        updateRequest = new ActionUpdateRequest("Updated note", 1L);
    }

    // -------------------------------------------------------------------------
    // createAction
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createAction should invalidate all caches after a successful save")
    void createAction_should_invalidateAllCaches_after_successfulSave() {
        // Arrange
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(agingVehicle));

        Action savedAction = new Action();
        savedAction.setId(UUID.randomUUID());
        savedAction.setVehicle(agingVehicle);
        savedAction.setStatus(createRequest.getStatus());
        savedAction.setType(createRequest.getType());
        savedAction.setNote(createRequest.getNote());
        savedAction.setCreatedBy("manager1");
        when(actionRepository.save(any(Action.class))).thenReturn(savedAction);

        // Act
        ActionResponse response = actionService.createAction(vehicleId, createRequest, "manager1");

        // Assert
        assertNotNull(response);
        verify(vehicleService).invalidateAllCaches();
    }

    @Test
    @DisplayName("createAction should NOT invalidate caches when vehicle is not aging")
    void createAction_should_notInvalidateCaches_when_vehicleIsNotAging() {
        // Arrange
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(newVehicle));

        // Act & Assert
        assertThrows(NotAgingVehicleException.class, () ->
                actionService.createAction(vehicleId, createRequest, "manager1"));

        verify(vehicleService, never()).invalidateAllCaches();
    }

    // -------------------------------------------------------------------------
    // updateAction
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("updateAction should invalidate all caches after a successful update")
    void updateAction_should_invalidateAllCaches_after_successfulUpdate() {
        // Arrange
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(agingVehicle));

        Action existingAction = new Action();
        existingAction.setId(actionId);
        existingAction.setVehicle(agingVehicle);
        existingAction.setStatus("PROPOSED");
        existingAction.setType("DISCOUNT");
        existingAction.setNote("Old note");
        existingAction.setVersion(1L);
        when(actionRepository.findById(actionId)).thenReturn(Optional.of(existingAction));
        when(actionRepository.save(any(Action.class))).thenReturn(existingAction);

        // Act
        actionService.updateAction(vehicleId, actionId, updateRequest, "manager1");

        // Assert
        verify(vehicleService).invalidateAllCaches();
    }

    @Test
    @DisplayName("updateAction should NOT invalidate caches when vehicle is not aging")
    void updateAction_should_notInvalidateCaches_when_vehicleIsNotAging() {
        // Arrange
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(newVehicle));

        // Act & Assert
        assertThrows(NotAgingVehicleException.class, () ->
                actionService.updateAction(vehicleId, actionId, updateRequest, "manager1"));

        verify(vehicleService, never()).invalidateAllCaches();
    }

    // -------------------------------------------------------------------------
    // deleteAction
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("deleteAction should invalidate all caches after a successful delete")
    void deleteAction_should_invalidateAllCaches_after_successfulDelete() {
        // Arrange
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(agingVehicle));

        Action existingAction = new Action();
        existingAction.setId(actionId);
        existingAction.setVehicle(agingVehicle);
        when(actionRepository.findById(actionId)).thenReturn(Optional.of(existingAction));

        // Act
        actionService.deleteAction(vehicleId, actionId);

        // Assert
        verify(vehicleService).invalidateAllCaches();
    }

    @Test
    @DisplayName("deleteAction should NOT invalidate caches when vehicle is not aging")
    void deleteAction_should_notInvalidateCaches_when_vehicleIsNotAging() {
        // Arrange
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(newVehicle));

        // Act & Assert
        assertThrows(NotAgingVehicleException.class, () ->
                actionService.deleteAction(vehicleId, actionId));

        verify(vehicleService, never()).invalidateAllCaches();
    }
}
