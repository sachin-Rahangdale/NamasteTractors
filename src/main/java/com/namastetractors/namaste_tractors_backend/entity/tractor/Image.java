package com.namastetractors.namaste_tractors_backend.entity.tractor;

import com.namastetractors.namaste_tractors_backend.emun.ImageType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tractor_images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String publicId;

    @Enumerated(EnumType.STRING) // 🔥 VERY IMPORTANT
    @Column(nullable = false)
    private ImageType imageType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tractor_id")
    private Tractor tractor;
}