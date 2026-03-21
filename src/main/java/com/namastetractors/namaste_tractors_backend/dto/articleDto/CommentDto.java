package com.namastetractors.namaste_tractors_backend.dto.articleDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {



    @NotBlank(message = "Comment content is required")
    @Size(min = 2, max = 500, message = "Comment must be between 2 and 500 characters")
    private String content;
}
