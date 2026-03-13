package com.namastetractors.namaste_tractors_backend.entity.tractor;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tractor")
public class Tractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private int hp;
    private double price;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToOne(mappedBy = "tractor", cascade = CascadeType.ALL,orphanRemoval = true)
    private TractorSpecification specification;
}
