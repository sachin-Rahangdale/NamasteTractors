package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import lombok.Data;

import java.util.List;

@Data
public class TractorResponseDto {

    private Long id;
    private String model;
    private int hp;
    private double price;

    private String brand;

    private TractorSpecDto specification;

    private List<ImageResponseDto> images;

}
