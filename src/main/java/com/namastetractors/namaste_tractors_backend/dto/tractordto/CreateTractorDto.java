package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import lombok.Data;

@Data
public class CreateTractorDto {
    private String model;
    private int hp;
    private double price;
    private Long brandId;
    private TractorSpecDto specification;
}
