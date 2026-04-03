package com.namastetractors.namaste_tractors_backend.dto.productDto;

import com.namastetractors.namaste_tractors_backend.emun.Category;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDetailDto {

    private Long id;

    private String productName;

    private Double price;

    private String unit;

    private String phone;

    private String city;

    private String pincode;

    private String description;

    private Category category;

    private LocalDateTime createdAt;

    private List<String> imageUrls; // 🔥 multiple images
}