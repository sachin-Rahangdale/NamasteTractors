package com.namastetractors.namaste_tractors_backend.entity.tractor;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tractor_images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tractorId;

    private String imageUrl;

    private String imageType;
}