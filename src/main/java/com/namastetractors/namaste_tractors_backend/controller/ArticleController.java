package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleCardDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleDetailDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CommentDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CommentResponseDto;
import com.namastetractors.namaste_tractors_backend.service.article.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleCardDto createArticle(

            @RequestParam("title")
            @NotBlank(message = "Title is required")
            @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
            String title,

            @RequestParam("content")
            @NotBlank(message = "Content is required")
            @Size(min = 20, message = "Content must be at least 20 characters")
            String content,

            @RequestParam("mainImage")
            @NotNull(message = "Main image is required")
            MultipartFile mainImage,

            Authentication authentication
    ){
        if (mainImage.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        return articleService.createArticle(title, content, mainImage, authentication);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImages(

            @PathVariable
            @NotNull(message = "Article ID is required")
            Long id,

            @RequestParam("images")
            @NotEmpty(message = "At least one image is required")
            List<MultipartFile> images
    ){
        // 🔥 extra safety
        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                throw new RuntimeException("One of the images is empty");
            }
        }

        articleService.uploadImages(id, images);
        return "Images uploaded successfully";
    }

    @GetMapping
    public Page<ArticleCardDto> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.getAllArticles(page, size);
    }

    @GetMapping("{slug}")
    public ArticleDetailDto getArticle(@PathVariable String slug){
        return articleService.getArticleBySlug(slug);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public Page<ArticleCardDto> getPendingArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.getPendingArticles(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public String approveArticle(

            @PathVariable
            @NotNull(message = "Article ID is required")
            Long id
    ){
        return articleService.approveArticle(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public String rejectArticle(

            @PathVariable
            @NotNull(message = "Article ID is required")
            Long id,

            @RequestParam
            @NotBlank(message = "Reject reason is required")
            @Size(min = 5, max = 200, message = "Reason must be between 5 and 200 characters")
            String reason
    ){
        return articleService.rejectArticle(id, reason);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ArticleDetailDto getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteArticle(

            @PathVariable
            @NotNull(message = "Article ID is required")
            Long id
    ){
        return articleService.deleteArticleById(id);
    }



    // comment


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentDto dto,
            Authentication auth
    ){
        return ResponseEntity.ok(articleService.addComment(id, dto, auth));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<CommentResponseDto> comments =
                articleService.getCommentsByArticleId(id, page, size);

        return ResponseEntity.ok(Map.of(
                "page", comments.getNumber(),
                "totalPages", comments.getTotalPages(),
                "totalElements", comments.getTotalElements(),
                "content", comments.getContent()
        ));
    }
}
