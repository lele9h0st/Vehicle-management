package com.hoang.keyloop.domain.vehicle;

import com.hoang.keyloop.controller.dto.PaginatedVehicleResponse;
import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.controller.dto.VehicleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);
    private static final String CACHE_NAME = "vehicles";

    private final VehicleRepository vehicleRepository;
    private final CacheManager cacheManager;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, CacheManager cacheManager) {
        this.vehicleRepository = vehicleRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, keyGenerator = "vehicleFilterCacheKeyGenerator")
    @Transactional(readOnly = true)
    public PaginatedVehicleResponse getVehicles(VehicleFilterRequest filterRequest) {
        logger.info("Cache MISS — fetching vehicles from DB");
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

    @Override
    public void invalidateAllCaches() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.clear();
            logger.info("Cache evicted — all '{}' entries cleared", CACHE_NAME);
        }
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

        boolean isAging = vehicle.getInventoryReceiptDate() != null &&
                vehicle.getInventoryReceiptDate().isBefore(java.time.LocalDate.now().minusDays(90));
        response.setIsAging(isAging);

        return response;
    }
}
