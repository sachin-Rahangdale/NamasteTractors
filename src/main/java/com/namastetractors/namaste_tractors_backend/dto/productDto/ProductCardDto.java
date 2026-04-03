package com.namastetractors.namaste_tractors_backend.dto.productDto;

import com.namastetractors.namaste_tractors_backend.emun.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductCardDto {

    private Long id;

    private String productName;

    private Double price;

    private String unit;

    private String city;

    private Category category;

    private String imageUrl; // 🔥 only 1 image (first image)

    private LocalDateTime createdAt;
}