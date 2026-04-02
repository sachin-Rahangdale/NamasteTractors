package com.namastetractors.namaste_tractors_backend.dto.articleDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleCardDto {

    private Long id;
    private String title;
    private String slug;
    private String mainImageUrl;
    private String author;
    private LocalDateTime createdAt;
    private String shortDescription;

}
