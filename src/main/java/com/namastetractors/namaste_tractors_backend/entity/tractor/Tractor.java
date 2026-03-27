package com.namastetractors.namaste_tractors_backend.entity.tractor;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tractors")
public class Tractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private int hp;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    // ✅ One-to-Many Images
    @OneToMany(mappedBy = "tractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>(); // ✅ FIX

    // ✅ One-to-One Specification
    @OneToOne(mappedBy = "tractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private TractorSpecification specification = new TractorSpecification();
}
