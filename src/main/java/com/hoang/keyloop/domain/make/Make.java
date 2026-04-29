package com.hoang.keyloop.domain.make;

import com.hoang.keyloop.domain.model.Model;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "make")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Make {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "make", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Model> models;

    public Make(String name) {
        this.name = name;
    }
}
