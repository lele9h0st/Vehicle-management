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
import java.time.LocalDateTime;
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

    private List<Vehicle> allVehicles;

    @BeforeEach
    void setUp() {
        allVehicles = prepareMockData();
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by make")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByMake() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setMake("Toyota");
        filterRequest.setPage(0);
        filterRequest.setSize(10);

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(0)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getMake()).isEqualTo("Toyota");
    }

    @Test
    @DisplayName("getVehicles should return all vehicles when no filter applied")
    void getVehicles_should_returnAllVehicles_when_noFilter() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setPage(0);
        filterRequest.setSize(20);

        Page<Vehicle> vehiclePage = new PageImpl<>(allVehicles, PageRequest.of(0, 20), allVehicles.size());
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(allVehicles.size());
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by model")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByModel() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setModel("Civic");

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getModel()).isEqualTo("Civic");
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by year")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByYear() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setYear(2022);

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getYear()).isEqualTo(2022);
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by status")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByStatus() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setStatus("SOLD");

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getStatus()).isEqualTo("SOLD");
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by price range")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByPriceRange() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setMinPrice(new BigDecimal("20000.00"));
        filterRequest.setMaxPrice(new BigDecimal("30000.00"));

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getPrice()).isBetween(new BigDecimal("20000.00"), new BigDecimal("30000.00"));
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by mileage range")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByMileageRange() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setMinMileage(4000);
        filterRequest.setMaxMileage(6000);

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getMileage()).isBetween(4000, 6000);
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by inventory receipt date")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByInventoryReceiptDate() {
        // Arrange
        LocalDate targetDate = LocalDate.now().minusDays(10);
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setInventoryReceiptDate(targetDate);

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getInventoryReceiptDate()).isEqualTo(targetDate);
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by updatedBy")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByUpdatedBy() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setUpdatedBy("admin");

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getUpdatedBy()).isEqualTo("admin");
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by createdAt")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByCreatedAt() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setCreatedAt(allVehicles.get(1).getCreatedAt());

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by updatedAt")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByUpdatedAt() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setUpdatedAt(allVehicles.get(1).getUpdatedAt());

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(1)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by new model Corolla")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByCorolla() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setModel("Corolla");

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(12)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getModel()).isEqualTo("Corolla");
    }

    @Test
    @DisplayName("getVehicles should return paginated vehicles when filtering by make with case insensitivity")
    void getVehicles_should_returnPaginatedVehicles_when_filteringByMakeCaseInsensitive() {
        // Arrange
        VehicleFilterRequest filterRequest = new VehicleFilterRequest();
        filterRequest.setMake("tOyOtA");

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(allVehicles.get(0)), PageRequest.of(0, 10), 1);
        when(vehicleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(vehiclePage);

        // Act
        PaginatedVehicleResponse response = vehicleService.getVehicles(filterRequest);

        // Assert
        assertThat(response.getVehicles()).hasSize(1);
        assertThat(response.getVehicles().get(0).getMake()).isEqualTo("Toyota");
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

    private List<Vehicle> prepareMockData() {
        Make toyota = new Make("Toyota");
        Make honda = new Make("Honda");
        Make ford = new Make("Ford");
        Make tesla = new Make("Tesla");
        Make bmw = new Make("BMW");

        Model camry = createModel("Camry", toyota);
        Model corolla = createModel("Corolla", toyota);
        Model civic = createModel("Civic", honda);
        Model accord = createModel("Accord", honda);
        Model f150 = createModel("F-150", ford);
        Model mustang = createModel("Mustang", ford);
        Model model3 = createModel("Model 3", tesla);
        Model series3 = createModel("3 Series", bmw);

        Vehicle v1 = createVehicle(camry, 2023, "VIN001", "AVAILABLE", new BigDecimal("35000"), 100, LocalDate.now());
        v1.setUpdatedBy("admin");
        v1.setCreatedAt(LocalDateTime.now().minusDays(1));
        v1.setUpdatedAt(LocalDateTime.now().minusDays(1));

        Vehicle v2 = createVehicle(civic, 2022, "VIN002", "SOLD", new BigDecimal("25000"), 5000, LocalDate.now().minusDays(10));
        v2.setUpdatedBy("admin");
        v2.setCreatedAt(LocalDateTime.now().minusDays(2));
        v2.setUpdatedAt(LocalDateTime.now().minusDays(2));

        Vehicle v3 = createVehicle(f150, 2021, "VIN003", "AVAILABLE", new BigDecimal("45000"), 20000, LocalDate.now().minusDays(20));
        Vehicle v4 = createVehicle(model3, 2023, "VIN004", "AVAILABLE", new BigDecimal("50000"), 500, LocalDate.now().minusDays(5));
        Vehicle v5 = createVehicle(series3, 2020, "VIN005", "AVAILABLE", new BigDecimal("30000"), 40000, LocalDate.now().minusDays(30));
        Vehicle v6 = createVehicle(camry, 2022, "VIN006", "AVAILABLE", new BigDecimal("32000"), 15000, LocalDate.now().minusDays(15));
        Vehicle v7 = createVehicle(civic, 2021, "VIN007", "AVAILABLE", new BigDecimal("22000"), 30000, LocalDate.now().minusDays(25));
        Vehicle v8 = createVehicle(f150, 2022, "VIN008", "SOLD", new BigDecimal("48000"), 10000, LocalDate.now().minusDays(12));
        Vehicle v9 = createVehicle(model3, 2022, "VIN009", "AVAILABLE", new BigDecimal("48000"), 8000, LocalDate.now().minusDays(8));
        Vehicle v10 = createVehicle(series3, 2021, "VIN010", "AVAILABLE", new BigDecimal("35000"), 25000, LocalDate.now().minusDays(22));
        Vehicle v11 = createVehicle(camry, 2021, "VIN011", "SOLD", new BigDecimal("28000"), 45000, LocalDate.now().minusDays(40));
        Vehicle v12 = createVehicle(civic, 2023, "VIN012", "AVAILABLE", new BigDecimal("26000"), 200, LocalDate.now().minusDays(2));
        Vehicle v13 = createVehicle(corolla, 2023, "VIN013", "AVAILABLE", new BigDecimal("23000"), 50, LocalDate.now());
        Vehicle v14 = createVehicle(accord, 2022, "VIN014", "AVAILABLE", new BigDecimal("28000"), 3000, LocalDate.now().minusDays(7));
        Vehicle v15 = createVehicle(mustang, 2023, "VIN015", "AVAILABLE", new BigDecimal("40000"), 1000, LocalDate.now().minusDays(3));
        Vehicle v16 = createVehicle(corolla, 2022, "VIN016", "SOLD", new BigDecimal("21000"), 12000, LocalDate.now().minusDays(15));

        return List.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16);
    }

    private Model createModel(String name, Make make) {
        Model model = new Model();
        model.setName(name);
        model.setMake(make);
        return model;
    }

    private Vehicle createVehicle(Model model, int year, String vin, String status, BigDecimal price, int mileage, LocalDate receiptDate) {
        Vehicle v = new Vehicle();
        v.setId(UUID.randomUUID());
        v.setModel(model);
        v.setYear(year);
        v.setVin(vin);
        v.setStatus(status);
        v.setPrice(price);
        v.setMileage(mileage);
        v.setInventoryReceiptDate(receiptDate);
        v.setCreatedAt(LocalDateTime.now());
        v.setUpdatedAt(LocalDateTime.now());
        return v;
    }
}
