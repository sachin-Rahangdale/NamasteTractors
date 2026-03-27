package com.namastetractors.namaste_tractors_backend.service.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.ImageResponseDto;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorImageDto;
import com.namastetractors.namaste_tractors_backend.emun.ImageType;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.ImageRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.TractorRepo;
import com.namastetractors.namaste_tractors_backend.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepo imageRepo;
    private final ImageUploadService imageUploadService;
    private final TractorRepo tractorRepo;

    @Transactional
    public ImageResponseDto createImage(TractorImageDto dto) {

        // 🔥 1. Validate file
        if (dto.getImage() == null || dto.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        String contentType = dto.getImage().getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image type");
        }

        // 🔥 2. Get tractor
        Tractor tractor = tractorRepo.findById(dto.getTractorId())
                .orElseThrow(() -> new RuntimeException("Tractor not found"));

        // 🔥 3. Enforce single MAIN image
        if (dto.getType() == ImageType.MAIN) {
            imageRepo.deleteByTractor_IdAndImageType(
                    tractor.getId(),
                    ImageType.MAIN
            );
        }

        // 🔥 4. Upload image
        String imageUrl = imageUploadService.uploadImage(dto.getImage());

        // 🔥 5. Save entity
        Image image = new Image();
        image.setImageType(dto.getType());
        image.setImageUrl(imageUrl);
        image.setTractor(tractor);

        Image savedImage = imageRepo.save(image);

        // 🔥 6. Map to DTO
        ImageResponseDto response = new ImageResponseDto();
        response.setImageUrl(savedImage.getImageUrl());
        response.setImageType(savedImage.getImageType());

        return response;
    }
}