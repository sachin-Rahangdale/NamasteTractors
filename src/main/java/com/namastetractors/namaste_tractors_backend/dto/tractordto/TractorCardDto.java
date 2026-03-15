package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import lombok.Data;

@Data
public class TractorCardDto {
    private Long id;
    private String model;
    private String brand;
    private int hp;
    private double price;
    private String imageUrl;

}
