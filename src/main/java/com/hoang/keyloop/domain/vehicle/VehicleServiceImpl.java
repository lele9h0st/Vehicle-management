package com.hoang.keyloop.domain.vehicle;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.controller.dto.VehicleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedVehicleResponse getVehicles(VehicleFilterRequest filterRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(filterRequest.getSortDirection()), filterRequest.getSortBy());
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), sort);

        Page<Vehicle> page = vehicleRepository.findAll(VehicleSpecification.withFilter(filterRequest), pageable);

        return new PaginatedVehicleResponse(
                page.getContent().stream().map(this::mapToResponse).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setMake(vehicle.getModel().getMake().getName());
        response.setModel(vehicle.getModel().getName());
        response.setYear(vehicle.getYear());
        response.setVin(vehicle.getVin());
        response.setStatus(vehicle.getStatus());
        response.setPrice(vehicle.getPrice());
        response.setMileage(vehicle.getMileage());
        response.setInventoryReceiptDate(vehicle.getInventoryReceiptDate());
        response.setCreatedAt(vehicle.getCreatedAt());
        response.setUpdatedBy(vehicle.getUpdatedBy());
        response.setUpdatedAt(vehicle.getUpdatedAt());
        return response;
    }
}
