package com.namastetractors.namaste_tractors_backend.dto.articleDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateArticleDto {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 20, message = "Content must be at least 20 characters")
    private String content;

    @NotNull(message = "Main image is required")
    private MultipartFile mainImage;

    @NotNull(message = "Author ID is required")
    private Long authorId;
}
