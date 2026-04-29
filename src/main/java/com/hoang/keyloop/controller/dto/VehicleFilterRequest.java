package com.hoang.keyloop.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // Getters and Setters
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public Integer getMinMileage() { return minMileage; }
    public void setMinMileage(Integer minMileage) { this.minMileage = minMileage; }

    public Integer getMaxMileage() { return maxMileage; }
    public void setMaxMileage(Integer maxMileage) { this.maxMileage = maxMileage; }

    public LocalDate getInventoryReceiptDate() { return inventoryReceiptDate; }
    public void setInventoryReceiptDate(LocalDate inventoryReceiptDate) { this.inventoryReceiptDate = inventoryReceiptDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}
