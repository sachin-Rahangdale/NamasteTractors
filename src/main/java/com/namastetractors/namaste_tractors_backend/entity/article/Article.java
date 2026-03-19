package com.namastetractors.namaste_tractors_backend.entity.article;

import com.namastetractors.namaste_tractors_backend.emun.Status;
import com.namastetractors.namaste_tractors_backend.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String slug;

    @Column(length = 10000)
    private String content;

    private Status status;

    private String mainImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
}
