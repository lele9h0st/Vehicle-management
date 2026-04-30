package com.hoang.keyloop.domain.action;

import com.hoang.keyloop.controller.dto.ActionRequest;
import com.hoang.keyloop.controller.dto.ActionResponse;
import com.hoang.keyloop.controller.dto.ActionUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ActionService {
    ActionResponse createAction(UUID vehicleId, ActionRequest request, String managerId);
    ActionResponse updateAction(UUID vehicleId, UUID actionId, ActionUpdateRequest request, String managerId);
    void deleteAction(UUID vehicleId, UUID actionId);
    List<ActionResponse> getActions(UUID vehicleId);
}
