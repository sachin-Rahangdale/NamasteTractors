package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleCardDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleDetailDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CommentDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CreateArticleDto;
import com.namastetractors.namaste_tractors_backend.service.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleCardDto createArticle(

            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("mainImage") MultipartFile mainImage,
            @RequestParam("authorId") Long authorId
    ){
        return articleService.createArticle(title, content, mainImage, authorId);
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImages(
            @PathVariable Long id,
            @RequestParam("images") List<MultipartFile> images
    ){
        articleService.uploadImages(id, images);
        return "Images uploaded successfully";
    }

    @GetMapping
    public List<ArticleCardDto> getAllArticles(){
        return articleService.getAllArticles();
    }

    @GetMapping("/{slug}")
    public ArticleDetailDto getArticle(@PathVariable String slug){
        return articleService.getArticleBySlug(slug);
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @RequestBody CommentDto dto){

        articleService.addComment(id, dto);
        return "Comment added";
    }
}
