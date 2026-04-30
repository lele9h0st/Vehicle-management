package com.hoang.keyloop.controller;

import com.hoang.keyloop.domain.vehicle.Vehicle;
import com.hoang.keyloop.domain.vehicle.VehicleRepository;
import com.hoang.keyloop.domain.vehicle.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Local-testing controller (replaces PingController).
 *
 * <p>Provides endpoints to manually trigger cache invalidation and inspect cached keys.
 * These endpoints are intended for development/testing only and should be
 * behind an auth guard or removed before production.
 */
@RestController
@RequestMapping
@Tag(name = "Test", description = "Local testing utilities")
public class TestController {

    private final VehicleRepository vehicleRepository;
    private final VehicleService vehicleService;
    private final RedisTemplate<String, Object> redisTemplate;

    public TestController(VehicleRepository vehicleRepository,
                          VehicleService vehicleService,
                          RedisTemplate<String, Object> redisTemplate) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/ping")
    @Operation(summary = "Health check")
    public String ping() {
        return "pong";
    }

    /**
     * Updates a vehicle's status and invalidates all vehicle caches.
     * Use this to manually test cache invalidation behaviour.
     */
    @PutMapping("/test/vehicle/{id}/status")
    @Operation(summary = "Update vehicle status (triggers cache invalidation)")
    public ResponseEntity<String> updateVehicleStatus(
            @PathVariable UUID id,
            @RequestParam String status) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + id));

        vehicle.setStatus(status);
        vehicleRepository.save(vehicle);
        vehicleService.invalidateAllCaches();

        return ResponseEntity.ok("Vehicle %s status updated to '%s'. Cache invalidated.".formatted(id, status));
    }

    /**
     * Returns all Redis keys that belong to the 'vehicles' cache.
     * Useful for inspecting what is currently cached.
     */
    @GetMapping("/test/cache/keys")
    @Operation(summary = "List all cached vehicle keys")
    public ResponseEntity<List<String>> getCacheKeys() {
        Set<String> keys = redisTemplate.keys("vehicles::*");
        List<String> sortedKeys = keys == null ? List.of() : keys.stream().sorted().toList();
        return ResponseEntity.ok(sortedKeys);
    }
}
