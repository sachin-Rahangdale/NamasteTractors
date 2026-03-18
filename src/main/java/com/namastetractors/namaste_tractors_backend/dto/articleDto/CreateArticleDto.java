package com.namastetractors.namaste_tractors_backend.dto.articleDto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateArticleDto {

    private String title;
    private String content;
    private MultipartFile mainImage;
    private Long authorId;
}
