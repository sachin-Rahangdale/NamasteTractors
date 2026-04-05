package com.namastetractors.namaste_tractors_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "enquiries")
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;
    private String phone;
    private String enquiryType;

    @Column(length = 1000)
    private String message;
    private String pincode;
    private String address;
    private LocalDateTime createdAt;
    private String progress;
    private LocalDateTime updatedAt;

}
