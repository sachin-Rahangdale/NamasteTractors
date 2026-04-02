package com.namastetractors.namaste_tractors_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnquiryRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phone;

    @NotBlank
    private String enquiryType;

    @NotBlank
    @Size(min = 5, max = 1000)
    private String message;

    @NotBlank
    private String pincode;

    @NotBlank
    private String address;
}
