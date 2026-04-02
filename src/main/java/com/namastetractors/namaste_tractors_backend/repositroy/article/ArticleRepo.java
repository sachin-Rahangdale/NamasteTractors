package com.namastetractors.namaste_tractors_backend.repositroy.article;

import com.namastetractors.namaste_tractors_backend.emun.Status;
import com.namastetractors.namaste_tractors_backend.entity.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepo extends JpaRepository<Article , Long> {
    Page<Article> findByStatus(Status status, Pageable pageable);

    Optional<Article> findBySlugAndStatus(String slug, Status status);


    @Query("""
SELECT a FROM Article a
JOIN FETCH a.author
WHERE a.status = :status
""")
    Page<Article> findByStatusWithAuthor(Status status, Pageable pageable);


}
