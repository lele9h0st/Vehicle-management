package com.hoang.keyloop.controller;

import com.hoang.keyloop.controller.dto.ActionRequest;
import com.hoang.keyloop.controller.dto.ActionResponse;
import com.hoang.keyloop.controller.dto.ActionUpdateRequest;
import com.hoang.keyloop.domain.action.ActionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory/action")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @PostMapping("/{vehicleId}")
    public ResponseEntity<ActionResponse> createAction(
            @PathVariable UUID vehicleId,
            @Valid @RequestBody ActionRequest request,
            @RequestHeader(value = "X-Manager-Id", defaultValue = "system") String managerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(actionService.createAction(vehicleId, request, managerId));
    }

    @PutMapping("/{vehicleId}/{actionId}")
    public ResponseEntity<ActionResponse> updateAction(
            @PathVariable UUID vehicleId,
            @PathVariable UUID actionId,
            @Valid @RequestBody ActionUpdateRequest request,
            @RequestHeader(value = "X-Manager-Id", defaultValue = "system") String managerId) {
        return ResponseEntity.ok(actionService.updateAction(vehicleId, actionId, request, managerId));
    }

    @DeleteMapping("/{vehicleId}/{actionId}")
    public ResponseEntity<Void> deleteAction(
            @PathVariable UUID vehicleId,
            @PathVariable UUID actionId) {
        actionService.deleteAction(vehicleId, actionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<List<ActionResponse>> getActions(@PathVariable UUID vehicleId) {
        return ResponseEntity.ok(actionService.getActions(vehicleId));
    }
}
