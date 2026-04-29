package com.hoang.keyloop.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {
    private UUID id;
    private String make;
    private String model;
    private Integer year;
    private String vin;
    private String status;
    private BigDecimal price;
    private Integer mileage;
    private LocalDate inventoryReceiptDate;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private Boolean isAging;
}
