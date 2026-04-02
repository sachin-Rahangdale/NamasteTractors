package com.namastetractors.namaste_tractors_backend.entity.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.namastetractors.namaste_tractors_backend.emun.Status;
import com.namastetractors.namaste_tractors_backend.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(length = 10000)
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String mainImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 🔥 AUTHOR
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    // 🔥 IMAGES
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ArticleImage> images = new ArrayList<>();

    // 🔥 COMMENTS
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();
}