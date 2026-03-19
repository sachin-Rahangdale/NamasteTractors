package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TractorImageDto {

    @NotNull(message = "Image file is required")
    private MultipartFile image;

    @NotBlank(message = "Image type is required")
    @Pattern(
            regexp = "^(MAIN|GALLERY|THUMBNAIL)$",
            message = "Type must be MAIN, GALLERY or THUMBNAIL"
    )
    private String type;

    @NotNull(message = "Tractor ID is required")
    private Long tractorId;
}
