package com.namastetractors.namaste_tractors_backend.dto.articleDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private String name;
    private String content;
    private LocalDateTime createdAt;
}
