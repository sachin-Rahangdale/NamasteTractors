package com.namastetractors.namaste_tractors_backend.dto;

import lombok.Data;

@Data
public class ImageUploadResult {

    // ✅ ADD THIS CONSTRUCTOR
    public ImageUploadResult(String url, String publicId) {
        this.url = url;
        this.publicId = publicId;
    }

    private String url;
    private String publicId;
}
