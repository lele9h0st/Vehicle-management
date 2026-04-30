package com.hoang.keyloop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoang.keyloop.controller.dto.ActionRequest;
import com.hoang.keyloop.controller.dto.ActionResponse;
import com.hoang.keyloop.controller.dto.ActionUpdateRequest;
import com.hoang.keyloop.domain.action.ActionService;
import com.hoang.keyloop.domain.exception.NotAgingVehicleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ActionControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ActionService actionService;

    @InjectMocks
    private ActionController actionController;

    private UUID vehicleId;
    private UUID actionId;
    private ActionRequest actionRequest;
    private ActionUpdateRequest actionUpdateRequest;
    private ActionResponse actionResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(actionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        vehicleId = UUID.randomUUID();
        actionId = UUID.randomUUID();

        actionRequest = new ActionRequest("PROPOSED", "DISCOUNT", "Reduce price by 5%");
        actionUpdateRequest = new ActionUpdateRequest("Reduce price by 10%", 1L);

        actionResponse = new ActionResponse(
                actionId,
                vehicleId,
                "PROPOSED",
                "DISCOUNT",
                "Reduce price by 5%",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "manager1",
                null,
                1L
        );
    }

    @Test
    void createAction_ShouldReturnCreated() throws Exception {
        when(actionService.createAction(eq(vehicleId), any(ActionRequest.class), eq("manager1")))
                .thenReturn(actionResponse);

        mockMvc.perform(post("/api/v1/inventory/action/{vehicleId}", vehicleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionRequest))
                        .header("X-Manager-Id", "manager1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(actionId.toString()))
                .andExpect(jsonPath("$.status").value("PROPOSED"));
    }

    @Test
    void createAction_WhenNotAgingVehicle_ShouldReturn422() throws Exception {
        when(actionService.createAction(eq(vehicleId), any(ActionRequest.class), anyString()))
                .thenThrow(new NotAgingVehicleException("Vehicle is not an aging vehicle"));

        mockMvc.perform(post("/api/v1/inventory/action/{vehicleId}", vehicleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionRequest))
                        .header("X-Manager-Id", "manager1"))
                .andExpect(status().is(422));
    }

    @Test
    void updateAction_WhenConcurrentModification_ShouldReturn409() throws Exception {
        when(actionService.updateAction(eq(vehicleId), eq(actionId), any(ActionUpdateRequest.class), anyString()))
                .thenThrow(new OptimisticLockingFailureException("Concurrent modification"));

        mockMvc.perform(put("/api/v1/inventory/action/{vehicleId}/{actionId}", vehicleId, actionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionUpdateRequest))
                        .header("X-Manager-Id", "manager1"))
                .andExpect(status().isConflict());
    }

    @Test
    void updateAction_WhenNotAgingVehicle_ShouldReturn422() throws Exception {
        when(actionService.updateAction(eq(vehicleId), eq(actionId), any(ActionUpdateRequest.class), anyString()))
                .thenThrow(new NotAgingVehicleException("Vehicle is not an aging vehicle"));

        mockMvc.perform(put("/api/v1/inventory/action/{vehicleId}/{actionId}", vehicleId, actionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionUpdateRequest))
                        .header("X-Manager-Id", "manager1"))
                .andExpect(status().is(422));
    }

    @Test
    void getActions_ShouldReturnList() throws Exception {
        when(actionService.getActions(vehicleId)).thenReturn(List.of(actionResponse));

        mockMvc.perform(get("/api/v1/inventory/action/{vehicleId}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(actionId.toString()));
    }

    @Test
    void getActions_WhenNotAgingVehicle_ShouldReturn422() throws Exception {
        when(actionService.getActions(vehicleId))
                .thenThrow(new NotAgingVehicleException("Vehicle is not an aging vehicle"));

        mockMvc.perform(get("/api/v1/inventory/action/{vehicleId}", vehicleId))
                .andExpect(status().is(422));
    }

    @Test
    void deleteAction_ShouldReturnNoContent() throws Exception {
        doNothing().when(actionService).deleteAction(vehicleId, actionId);

        mockMvc.perform(delete("/api/v1/inventory/action/{vehicleId}/{actionId}", vehicleId, actionId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAction_WhenNotAgingVehicle_ShouldReturn422() throws Exception {
        doThrow(new NotAgingVehicleException("Vehicle is not an aging vehicle"))
                .when(actionService).deleteAction(vehicleId, actionId);

        mockMvc.perform(delete("/api/v1/inventory/action/{vehicleId}/{actionId}", vehicleId, actionId))
                .andExpect(status().is(422));
    }
}
