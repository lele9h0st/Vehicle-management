package com.hoang.keyloop.domain.vehicle;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;

public interface VehicleService {
    PaginatedVehicleResponse getVehicles(VehicleFilterRequest filterRequest);
    void invalidateAllCaches();
}
