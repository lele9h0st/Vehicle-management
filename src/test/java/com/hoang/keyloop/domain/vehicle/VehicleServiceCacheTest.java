package com.hoang.keyloop.domain.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.domain.make.Make;
import com.hoang.keyloop.domain.model.Model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class VehicleServiceCacheTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache vehiclesCache;

    private VehicleServiceImpl vehicleService;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleServiceImpl(vehicleRepository, cacheManager);
    }

    // -------------------------------------------------------------------------
    // invalidateAllCaches
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("invalidateAllCaches should clear the 'vehicles' cache")
    void invalidateAllCaches_should_clearVehiclesCache() {
        // Arrange
        when(cacheManager.getCache("vehicles")).thenReturn(vehiclesCache);

        // Act
        vehicleService.invalidateAllCaches();

        // Assert
        verify(cacheManager).getCache("vehicles");
        verify(vehiclesCache).clear();
    }

    @Test
    @DisplayName("invalidateAllCaches should do nothing when cache is not registered")
    void invalidateAllCaches_should_doNothing_when_cacheNotRegistered() {
        // Arrange
        when(cacheManager.getCache("vehicles")).thenReturn(null);

        // Act & Assert — must not throw
        vehicleService.invalidateAllCaches();

        verify(vehiclesCache, never()).clear();
    }

    // -------------------------------------------------------------------------
    // getVehicles — the method body executes on cache miss (no real Redis here)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getVehicles should delegate to vehicleRepository when called (cache-miss path)")
    void getVehicles_should_delegateToRepository_on_cacheMiss() {
        // Arrange
        Make toyota = new Make("Toyota");
        Model camry = new Model();
        camry.setName("Camry");
        camry.setMake(toyota);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setModel(camry);
        vehicle.setYear(2023);
        vehicle.setVin("VIN001");
        vehicle.setStatus("AVAILABLE");
        vehicle.setPrice(new BigDecimal("35000"));
        vehicle.setMileage(100);
        vehicle.setInventoryReceiptDate(LocalDate.now());

        Page<Vehicle> page = new PageImpl<>(List.of(vehicle), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        VehicleFilterRequest request = new VehicleFilterRequest();
        request.setPage(0);
        request.setSize(10);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getVehicles()).hasSize(1);
        verify(vehicleRepository).findAll(any(Specification.class), any(PageRequest.class));
    }

}
