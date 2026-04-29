package com.hoang.keyloop.domain.vehicle;

import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import com.hoang.keyloop.domain.make.Make;
import com.hoang.keyloop.domain.model.Model;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class VehicleSpecification {

    public static Specification<Vehicle> withFilter(VehicleFilterRequest filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (StringUtils.hasText(filter.getMake())) {
                Join<Vehicle, Model> modelJoin = root.join("model");
                Join<Model, Make> makeJoin = modelJoin.join("make");
                predicates = cb.and(predicates, cb.equal(cb.lower(makeJoin.get("name")), filter.getMake().toLowerCase()));
            }

            if (StringUtils.hasText(filter.getModel())) {
                Join<Vehicle, Model> modelJoin = root.join("model");
                predicates = cb.and(predicates, cb.equal(cb.lower(modelJoin.get("name")), filter.getModel().toLowerCase()));
            }

            if (filter.getYear() != null) {
                predicates = cb.and(predicates, cb.equal(root.get("year"), filter.getYear()));
            }

            if (StringUtils.hasText(filter.getStatus())) {
                predicates = cb.and(predicates, cb.equal(cb.lower(root.get("status")), filter.getStatus().toLowerCase()));
            }

            if (filter.getMinPrice() != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null) {
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            if (filter.getMinMileage() != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("mileage"), filter.getMinMileage()));
            }

            if (filter.getMaxMileage() != null) {
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("mileage"), filter.getMaxMileage()));
            }

            if (filter.getInventoryReceiptDate() != null) {
                predicates = cb.and(predicates, cb.equal(root.get("inventoryReceiptDate"), filter.getInventoryReceiptDate()));
            }

            if (filter.getCreatedAt() != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAt()));
            }

            if (StringUtils.hasText(filter.getUpdatedBy())) {
                predicates = cb.and(predicates, cb.equal(cb.lower(root.get("updatedBy")), filter.getUpdatedBy().toLowerCase()));
            }

            if (filter.getUpdatedAt() != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("updatedAt"), filter.getUpdatedAt()));
            }

            if (Boolean.TRUE.equals(filter.getIsAging())) {
                java.time.LocalDate agingDate = java.time.LocalDate.now().minusDays(90);
                predicates = cb.and(predicates, cb.lessThan(root.get("inventoryReceiptDate"), agingDate));
            }

            if (filter.getMinDaysInInventory() != null) {
                java.time.LocalDate minDate = java.time.LocalDate.now().minusDays(filter.getMinDaysInInventory());
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("inventoryReceiptDate"), minDate));
            }

            if (filter.getMaxDaysInInventory() != null) {
                java.time.LocalDate maxDate = java.time.LocalDate.now().minusDays(filter.getMaxDaysInInventory());
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("inventoryReceiptDate"), maxDate));
            }

            return predicates;
        };
    }
}
