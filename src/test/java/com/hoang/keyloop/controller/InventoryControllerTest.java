package com.hoang.keyloop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.controller.dto.VehicleResponse;
import com.hoang.keyloop.domain.vehicle.VehicleService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/inventory/stock should return 200 and paginated response")
    void getStock_should_return200AndPaginatedResponse() throws Exception {
        // Arrange
        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setMake("Toyota");
        vehicleResponse.setModel("Camry");

        PaginatedVehicleResponse response = new PaginatedVehicleResponse(
                List.of(vehicleResponse), 1, 1, 0, 10);

        when(vehicleService.getVehicles(any(VehicleFilterRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/inventory/stock")
                        .param("make", "Toyota")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicles").isArray())
                .andExpect(jsonPath("$.vehicles[0].make").value("Toyota"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/inventory/stock should return 400 when invalid parameters provided")
    void getStock_should_return400_when_invalidParams() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/inventory/stock")
                        .param("size", "200") // Max is 100
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
