package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBrandDto {

    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Brand name must not contain special characters")
    private String name;
}
