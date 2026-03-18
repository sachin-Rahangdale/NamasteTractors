package com.namastetractors.namaste_tractors_backend.dto.articleDto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDetailDto {

    private String title;
    private String content;
    private String mainImageUrl;
    private String author;
    private LocalDateTime createdAt;
    private List<String> images;
}
