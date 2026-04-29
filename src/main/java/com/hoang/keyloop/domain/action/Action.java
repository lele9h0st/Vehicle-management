package com.hoang.keyloop.domain.action;

import com.hoang.keyloop.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(length = 50)
    private String status;

    @Column(length = 50)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;
}
