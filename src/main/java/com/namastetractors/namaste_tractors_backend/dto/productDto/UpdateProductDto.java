package com.namastetractors.namaste_tractors_backend.dto.productDto;

import lombok.Data;

@Data
public class UpdateProductDto {

    private Double price;
    private String description;
    private String city;
}
