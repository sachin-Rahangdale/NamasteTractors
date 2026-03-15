package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TractorImageDto {

    private MultipartFile image;
    private String type;
    private Long tractorId;
}
