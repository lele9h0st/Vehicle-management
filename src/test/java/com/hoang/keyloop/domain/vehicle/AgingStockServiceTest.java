package com.hoang.keyloop.domain.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.controller.dto.VehicleResponse;
import com.hoang.keyloop.domain.make.Make;
import com.hoang.keyloop.domain.model.Model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
class AgingStockServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    @DisplayName("getVehicles should correctly identify aging stock (> 90 days)")
    void getVehicles_should_identifyAgingStock() {
        // Arrange
        LocalDate today = LocalDate.now();
        Vehicle agingVehicle = createVehicle(today.minusDays(91));
        Vehicle nonAgingVehicle1 = createVehicle(today.minusDays(90));
        Vehicle nonAgingVehicle2 = createVehicle(today.minusDays(89));

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(agingVehicle, nonAgingVehicle1, nonAgingVehicle2), PageRequest.of(0, 10), 3);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(new VehicleFilterRequest());

        // Assert
        List<VehicleResponse> vehicles = response.getVehicles();
        assertThat(vehicles).hasSize(3);
        assertThat(vehicles.get(0).getIsAging()).isTrue();   // 91 days
        assertThat(vehicles.get(1).getIsAging()).isFalse();  // 90 days
        assertThat(vehicles.get(2).getIsAging()).isFalse();  // 89 days
    }

    private Vehicle createVehicle(LocalDate receiptDate) {
        Vehicle v = new Vehicle();
        v.setId(UUID.randomUUID());
        v.setInventoryReceiptDate(receiptDate);
        v.setPrice(BigDecimal.TEN);
        v.setMileage(100);
        v.setYear(2023);
        v.setStatus("AVAILABLE");
        v.setVin(UUID.randomUUID().toString().replace("-", "").substring(0, 17));
        
        Model model = new Model();
        model.setName("Model");
        model.setMake(new Make("Make"));
        v.setModel(model);
        
        return v;
    }
}
