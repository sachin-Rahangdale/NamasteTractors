package com.namastetractors.namaste_tractors_backend.entity.tractor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Tractor> tractors;
}
