package com.hoang.keyloop.domain.model;

import com.hoang.keyloop.domain.make.Make;
import com.hoang.keyloop.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "make_id", nullable = false)
    private Make make;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;
}
