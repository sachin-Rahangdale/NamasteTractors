package com.namastetractors.namaste_tractors_backend.repositroy.article;

import com.namastetractors.namaste_tractors_backend.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepo extends JpaRepository<Article , Long> {
    Optional<Article> findBySlug(String slug);
}
