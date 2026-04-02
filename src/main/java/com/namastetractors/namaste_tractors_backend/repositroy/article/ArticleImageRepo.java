package com.namastetractors.namaste_tractors_backend.repositroy.article;

import com.namastetractors.namaste_tractors_backend.entity.article.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleImageRepo extends JpaRepository<ArticleImage, Long> {

}
