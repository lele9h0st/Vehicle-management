package com.hoang.keyloop.controller.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedVehicleResponse {
    private List<VehicleResponse> vehicles;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
}
