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
    @DisplayName("Should return 429 Too Many Requests when exceeding rate limit for a single IP")
    void should_return429_when_rateLimitExceededPerIp() throws Exception {
        String clientIp = "192.168.1.1";
        
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(get("/api/v1/inventory/stock")
                            .with(request -> {
                                request.setRemoteAddr(clientIp);
                                return request;
                            }))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/v1/inventory/stock")
                        .with(request -> {
                            request.setRemoteAddr(clientIp);
                            return request;
                        }))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("Should allow requests from different IPs even if one is rate limited")
    void should_allowDifferentIps_when_oneIsLimited() throws Exception {
        String limitedIp = "192.168.1.1";
        String allowedIp = "192.168.1.2";

        // Exceed limit for limitedIp
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(get("/api/v1/inventory/stock")
                            .with(request -> {
                                request.setRemoteAddr(limitedIp);
                                return request;
                            }))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/v1/inventory/stock")
                        .with(request -> {
                            request.setRemoteAddr(limitedIp);
                            return request;
                        }))
                .andExpect(status().isTooManyRequests());

        // allowedIp should still be able to make requests
        mockMvc.perform(get("/api/v1/inventory/stock")
                        .with(request -> {
                            request.setRemoteAddr(allowedIp);
                            return request;
                        }))
                .andExpect(status().isOk());
    }
}
