package com.namastetractors.namaste_tractors_backend.entity.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.namastetractors.namaste_tractors_backend.emun.Category;
import com.namastetractors.namaste_tractors_backend.entity.User;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private LocalDateTime createdAt;

    private Double price;//numeric value

    private String unit;//kg,quintal

    private String phone;

    private String city;

    private String pincode;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval=true)
    @JsonIgnore
    private List<ProductImage> images = new ArrayList<>();

}








