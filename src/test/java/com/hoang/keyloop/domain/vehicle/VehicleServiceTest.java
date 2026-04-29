package com.hoang.keyloop.domain.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private Make toyota;
    private Make honda;
    private Model camry;
    private Model civic;

    @BeforeEach
    void setUp() {
        toyota = new Make();
        toyota.setName("Toyota");

        honda = new Make();
        honda.setName("Honda");

        camry = new Model();
        camry.setName("Camry");
        camry.setMake(toyota);

        civic = new Model();
        civic.setName("Civic");
        civic.setMake(honda);

        vehicle1 = new Vehicle();
        vehicle1.setId(UUID.randomUUID());
        vehicle1.setModel(camry);
        vehicle1.setYear(2023);
        vehicle1.setVin("12345678901234567");
        vehicle1.setStatus("AVAILABLE");
        vehicle1.setPrice(new BigDecimal("35000.00"));
        vehicle1.setMileage(100);
        vehicle1.setInventoryReceiptDate(LocalDate.now());

        vehicle2 = new Vehicle();
        vehicle2.setId(UUID.randomUUID());
        vehicle2.setModel(civic);
        vehicle2.setYear(2022);
        vehicle2.setVin("76543210987654321");
        vehicle2.setStatus("SOLD");
        vehicle2.setPrice(new BigDecimal("25000.00"));
        vehicle2.setMileage(5000);
        vehicle2.setInventoryReceiptDate(LocalDate.now().minusDays(10));
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by make")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByMake() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setMake("Toyota");
        filterRequest.setPage(0);
        filterRequest.setSize(10);

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle1), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getMake()).isEqualTo("Toyota");
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("getVehicles should return both vehicles when no filter applied")
    void getVehicles_should_returnAllVehicles_when_noFilter() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setPage(0);
        filterRequest.setSize(10);

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle1, vehicle2), PageRequest.of(0, 10), 2);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("getVehicles should return empty list when no vehicles match criteria")
    void getVehicles_should_returnEmptyList_when_noVehiclesMatch() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setMake("NonExistent");

        Page<Vehicle> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(emptyPage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).isEmpty();
        assertThat(response.getTotalElements()).isZero();
    }
}
