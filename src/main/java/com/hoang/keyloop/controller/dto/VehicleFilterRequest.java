package com.hoang.keyloop.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VehicleFilterRequest {
    @Schema(description = "Vehicle make", example = "Toyota")
    private String make;

    @Schema(description = "Vehicle model", example = "Camry")
    private String model;

    @Schema(description = "Manufacturing year", example = "2023")
    private Integer year;

    @Schema(description = "Vehicle status", example = "AVAILABLE")
    private String status;

    @Schema(description = "Minimum price")
    private BigDecimal minPrice;

    @Schema(description = "Maximum price")
    private BigDecimal maxPrice;

    @Schema(description = "Minimum mileage")
    private Integer minMileage;

    @Schema(description = "Maximum mileage")
    private Integer maxMileage;

    @Schema(description = "Inventory receipt date", example = "2024-04-29")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inventoryReceiptDate;

    @Schema(description = "Record creation timestamp")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Schema(description = "User who last updated the record", example = "admin")
    private String updatedBy;

    @Schema(description = "Last update timestamp")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;

    @Schema(description = "Page number (zero-based)", defaultValue = "0")
    @Min(0)
    private int page = 0;

    @Schema(description = "Records per page", defaultValue = "10", minimum = "1", maximum = "100")
    @Min(1)
    @Max(100)
    private int size = 10;

    @Schema(description = "Field to sort by", defaultValue = "createdAt")
    private String sortBy = "createdAt";

    @Schema(description = "Sort direction", defaultValue = "DESC")
    private String sortDirection = "DESC";

    @Schema(description = "Filter by aging stock (> 90 days)", example = "true")
    private Boolean isAging;

    @Schema(description = "Minimum days in inventory", example = "30")
    @Min(0)
    private Integer minDaysInInventory;

    @Schema(description = "Maximum days in inventory", example = "120")
    @Min(0)
    private Integer maxDaysInInventory;

}
