package com.namastetractors.namaste_tractors_backend.dto;

import com.namastetractors.namaste_tractors_backend.entity.tractor.TractorSpecification;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateTractorDto {
    private String model;
    private int hp;
    private double price;
    private Long brandId;
    private MultipartFile image;
    private TractorSpecDto tractorSpecDto;

}
