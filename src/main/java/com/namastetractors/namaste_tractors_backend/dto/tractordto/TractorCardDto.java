package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TractorCardDto {

    private Long id;
    private String model;
    private String brand;
    private int hp;
    private BigDecimal price;

    private String imageUrl; // MAIN or THUMBNAIL
}
