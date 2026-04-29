package com.hoang.keyloop.controller;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.domain.vehicle.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory", description = "API for managing vehicle inventory")
public class InventoryController {

    private final VehicleService vehicleService;

    public InventoryController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(
            summary = "Get filterable list of all vehicles",
            description = "Returns a paginated list of vehicles based on various filters like make, model, year, price, etc."
    )
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "429", description = "Too many requests - Rate limit exceeded")
    @GetMapping("/stock")
    public ResponseEntity<PaginatedVehicleResponse> getStock(@Valid VehicleFilterRequest filterRequest) {
        // Stub implementation
        return ResponseEntity.ok(vehicleService.getVehicles(filterRequest));
    }
}
