package com.namastetractors.namaste_tractors_backend.entity.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.namastetractors.namaste_tractors_backend.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    // 🔥 ARTICLE RELATION
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Article article;

    // 🔥 USER RELATION
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}