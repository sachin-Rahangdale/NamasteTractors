package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import com.namastetractors.namaste_tractors_backend.emun.ImageType;
import lombok.Data;

@Data
public class ImageResponseDto {

    private String imageUrl;
    private ImageType imageType;
}
