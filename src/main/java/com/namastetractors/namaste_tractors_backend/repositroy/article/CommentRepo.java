package com.namastetractors.namaste_tractors_backend.repositroy.article;

import com.namastetractors.namaste_tractors_backend.entity.article.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CommentRepo extends JpaRepository <Comment,Long> {
    Page<Comment> findByArticleId(Long articleId, Pageable pageable);

    void deleteByArticleId(Long articleId);
}
