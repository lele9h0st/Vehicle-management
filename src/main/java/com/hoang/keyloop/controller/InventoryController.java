package com.hoang.keyloop.controller;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.domain.vehicle.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory/stock")
public class InventoryController {

    private final VehicleService vehicleService;

    public InventoryController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<PaginatedVehicleResponse> getStock(@Valid VehicleFilterRequest filterRequest) {
        // Stub implementation
        return ResponseEntity.ok(vehicleService.getVehicles(filterRequest));
    }
}
