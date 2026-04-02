package com.namastetractors.namaste_tractors_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnquiryResponseDto {

    private Long id;
    private String name;
    private String phone;
    private String enquiryType;
    private String message;
    private String pincode;
    private String address;
    private LocalDateTime createdAt;
}
