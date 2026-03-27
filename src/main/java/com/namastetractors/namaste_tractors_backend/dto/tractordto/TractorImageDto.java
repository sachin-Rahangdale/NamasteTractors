package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import com.namastetractors.namaste_tractors_backend.emun.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TractorImageDto {

    @NotNull(message = "Image file is required")
    private MultipartFile image;

    @NotNull(message = "Image type is required")
    private ImageType type;

    @NotNull(message = "Tractor ID is required")
    @Positive(message = "Tractor ID must be positive")
    private Long tractorId;
}