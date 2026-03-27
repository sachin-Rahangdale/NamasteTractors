package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTractorDto {

    @NotBlank(message = "Model is required")
    @Size(min = 2, max = 100, message = "Model must be between 2 and 100 characters")
    private String model;

    @Min(value = 10, message = "HP must be at least 10")
    @Max(value = 200, message = "HP cannot exceed 200")
    private int hp;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "50000.0", message = "Price must be at least ₹50,000")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    private BigDecimal price;

    @NotNull(message = "Brand ID is required")
    @Positive(message = "Brand ID must be positive")
    private Long brandId;

    @NotNull(message = "Specification is required")
    @Valid
    private TractorSpecDto specification;
}