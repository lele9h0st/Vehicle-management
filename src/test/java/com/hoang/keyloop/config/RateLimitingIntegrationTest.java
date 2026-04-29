package com.hoang.keyloop.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hoang.keyloop.controller.InventoryController;
import com.hoang.keyloop.domain.vehicle.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class RateLimitingIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new InventoryController(vehicleService))
                .addFilters(new RateLimitingFilter())
                .build();
    }

    @Test
    @DisplayName("Should return 429 Too Many Requests when exceeding rate limit")
    void should_return429_when_rateLimitExceeded() throws Exception {
        // We have a limit of 20 requests per 10 seconds.
        // Let's send 21 requests.
        
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(get("/api/v1/inventory/stock"))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/v1/inventory/stock"))
                .andExpect(status().isTooManyRequests());
    }
}
