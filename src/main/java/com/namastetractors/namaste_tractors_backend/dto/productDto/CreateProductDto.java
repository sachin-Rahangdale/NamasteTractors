package com.namastetractors.namaste_tractors_backend.dto.productDto;
import com.namastetractors.namaste_tractors_backend.emun.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProductDto {

    @NotBlank
    private String productName;

    @NotNull
    private Double price;

    @NotBlank
    private String unit; // kg, quintal, piece

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phone;

    @NotBlank
    private String city;

    @NotBlank
    private String pincode;

    @NotBlank
    @Size(min = 5, max = 1000)
    private String description;

    @NotNull
    private Category category;
}
