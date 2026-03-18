package com.namastetractors.namaste_tractors_backend.service.article;

import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleCardDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleDetailDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CommentDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CreateArticleDto;
import com.namastetractors.namaste_tractors_backend.entity.User;
import com.namastetractors.namaste_tractors_backend.entity.article.Article;
import com.namastetractors.namaste_tractors_backend.entity.article.ArticleImage;
import com.namastetractors.namaste_tractors_backend.entity.article.Comment;
import com.namastetractors.namaste_tractors_backend.repositroy.UserRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.article.ArticleImageRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.article.ArticleRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.article.CommentRepo;
import com.namastetractors.namaste_tractors_backend.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private ArticleImageRepo articleImageRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ImageUploadService imageUploadService;

    public ArticleCardDto createArticle(
            String title,
            String content,
            MultipartFile mainImage,
            Long authorId
    ){

        // 1️⃣ get user
        User user = userRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ upload image
        String imageUrl = imageUploadService.uploadImage(mainImage);

        // 3️⃣ create slug
        String slug = title.toLowerCase().replace(" ", "-");

        // 4️⃣ create entity
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setMainImageUrl(imageUrl);
        article.setSlug(slug);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setAuthor(user);

        Article saved = articleRepo.save(article);

        // 5️⃣ response DTO
        ArticleCardDto res = new ArticleCardDto();
        res.setId(saved.getId());
        res.setTitle(saved.getTitle());
        res.setSlug(saved.getSlug());
        res.setMainImageUrl(saved.getMainImageUrl());
        res.setAuthor(user.getUsername());
        res.setCreatedAt(saved.getCreatedAt());

        return res;
    }


    public void uploadImages(Long articleId, List<MultipartFile> files){

        for(MultipartFile file : files){

            String url = imageUploadService.uploadImage(file);

            ArticleImage img = new ArticleImage();
            img.setArticleId(articleId);
            img.setImageUrl(url);

            articleImageRepo.save(img);
        }
    }

    public List<ArticleCardDto> getAllArticles(){

        List<Article> articles = articleRepo.findAll();

        return articles.stream().map(article -> {

            ArticleCardDto dto = new ArticleCardDto();

            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            dto.setSlug(article.getSlug());
            dto.setMainImageUrl(article.getMainImageUrl());
            dto.setAuthor(article.getAuthor().getUsername());
            dto.setCreatedAt(article.getCreatedAt());

            return dto;

        }).toList();
    }

    public ArticleDetailDto getArticleBySlug(String slug){

        Article article = articleRepo.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        List<String> images = articleImageRepo.findByArticleId(article.getId())
                .stream()
                .map(ArticleImage::getImageUrl)
                .toList();

        ArticleDetailDto dto = new ArticleDetailDto();

        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setMainImageUrl(article.getMainImageUrl());
        dto.setAuthor(article.getAuthor().getUsername());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setImages(images);

        return dto;
    }

    public void addComment(Long articleId, CommentDto dto){

        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUsername(dto.getUsername());
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepo.save(comment);
    }

    public String deleteArticleById(Long id){
        articleRepo.findById(id).orElseThrow(()->new RuntimeException("Article Not Found"));
        articleRepo.deleteById(id);
        return "Article Deleted Successfully";

    }

    public ArticleDetailDto

    //add get comment by article id method also improve addcomment after securizing api

}
