package com.namastetractors.namaste_tractors_backend.repositroy.article;

import com.namastetractors.namaste_tractors_backend.entity.article.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository <Comment,Long> {
    List<Comment> findByArticleId(Long articleId);
}
