package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TractorResponseDto {

    private Long id;
    private String model;
    private int hp;
    private BigDecimal price;

    private String brand;

    private TractorSpecDto specification;

    private List<ImageResponseDto> images;
}
