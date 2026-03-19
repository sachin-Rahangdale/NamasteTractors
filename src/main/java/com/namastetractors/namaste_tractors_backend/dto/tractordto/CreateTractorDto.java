package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateTractorDto {

    @NotBlank(message = "Model is required")
    @Size(min = 2, max = 100, message = "Model must be between 2 and 100 characters")
    private String model;

    @Min(value = 10, message = "HP must be at least 10")
    @Max(value = 200, message = "HP cannot exceed 200")
    private int hp;

    @Min(value = 50000, message = "Price must be at least ₹50,000")
    private double price;

    @NotNull(message = "Brand ID is required")
    private Long brandId;

    @NotNull(message = "Specification is required")
    @Valid   // 🔥 nested validation enable
    private TractorSpecDto specification;
}
