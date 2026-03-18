package com.namastetractors.namaste_tractors_backend.entity.article;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "article_images")
public class ArticleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long articleId;

    private String imageUrl;
}
