package com.namastetractors.namaste_tractors_backend.service.article;

import com.namastetractors.namaste_tractors_backend.dto.ImageUploadResult;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleCardDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.ArticleDetailDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CommentDto;
import com.namastetractors.namaste_tractors_backend.dto.articleDto.CommentResponseDto;
import com.namastetractors.namaste_tractors_backend.emun.Role;
import com.namastetractors.namaste_tractors_backend.emun.Status;
import com.namastetractors.namaste_tractors_backend.entity.User;
import com.namastetractors.namaste_tractors_backend.entity.article.Article;
import com.namastetractors.namaste_tractors_backend.entity.article.ArticleImage;
import com.namastetractors.namaste_tractors_backend.entity.article.Comment;
import com.namastetractors.namaste_tractors_backend.repositroy.UserRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.article.ArticleImageRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.article.ArticleRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.article.CommentRepo;
import com.namastetractors.namaste_tractors_backend.service.ImageUploadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
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
            Authentication auth
    ){
        String username = auth.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (mainImage == null || mainImage.isEmpty()){
            throw new RuntimeException("Main Image is Required");
        }

        // 🔥 FIXED: handle ImageUploadResult
        ImageUploadResult uploadResult = imageUploadService.uploadImage(mainImage);
        String mainUrl = uploadResult.getUrl();

        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");

        String slug = baseSlug + "-" + System.currentTimeMillis();

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setMainImageUrl(mainUrl); // ✅ using url
        article.setSlug(slug);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setAuthor(user);
        article.setStatus(user.getRole() == Role.ADMIN ? Status.APPROVED : Status.PENDING);

        Article saved = articleRepo.save(article);
        return mapToCardDto(saved);
    }

    private ArticleCardDto mapToCardDto(Article article){
        ArticleCardDto dto = new ArticleCardDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setSlug(article.getSlug());
        dto.setAuthor(
                article.getAuthor().getName() != null
                        ? article.getAuthor().getName()
                        : article.getAuthor().getUsername().split("@")[0]
        );
        dto.setCreatedAt(article.getCreatedAt());
        dto.setMainImageUrl(article.getMainImageUrl());
        String content = article.getContent();
        dto.setShortDescription(content.length()>120 ? content.substring(0,120)+"...":content);
        return dto;
    }


    @Transactional
    public void uploadImages(Long articleId, List<MultipartFile> files, Authentication auth){

        String username = auth.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Article article = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("No Article Found Article Id is Incorrect"));

        // 🔐 Ownership check (IMPORTANT)
        if (!article.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to modify this article");
        }

        for(MultipartFile file : files){

            // 🔥 FIXED: handle ImageUploadResult
            ImageUploadResult result = imageUploadService.uploadImage(file);

            ArticleImage img = new ArticleImage();
            img.setArticle(article);
            img.setImageUrl(result.getUrl());        // ✅ URL
            img.setPublicId(result.getPublicId());   // ✅ PUBLIC ID

            article.getImages().add(img);
        }
    }

    public Page<ArticleCardDto> getAllArticles(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return articleRepo.findByStatusWithAuthor(Status.APPROVED, pageable)
                .map(this::mapToCardDto);
    }

    public ArticleDetailDto getArticleBySlug(String slug){

        Article article = articleRepo.findBySlugAndStatus(slug, Status.APPROVED)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        return mapToDetailDto(article);
    }

    public ArticleDetailDto mapToDetailDto(Article article){
        ArticleDetailDto dto =  new ArticleDetailDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setMainImageUrl(article.getMainImageUrl());
        dto.setAuthor(article.getAuthor().getName());
        dto.setImages(article.getImages()
                .stream()
                .map(ArticleImage::getImageUrl)
                .toList());

        dto.setComments(article.getComments()
                .stream()
                .map(this :: mapToCommentDto)
                .toList());

        return dto;
    }

    private CommentResponseDto mapToCommentDto(Comment comment){

        CommentResponseDto dto = new CommentResponseDto();

        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setName(
                comment.getUser().getName() != null
                        ? comment.getUser().getName()
                        : comment.getUser().getUsername().split("@")[0]
        );
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }



    @Transactional
    public String approveArticle(Long id){

        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if(article.getStatus() == Status.APPROVED){
            return "Article already approved";
        }

        article.setStatus(Status.APPROVED);
        article.setUpdatedAt(LocalDateTime.now());

        return "Article approved successfully";
    }

    @Transactional
    public String rejectArticle(Long id, String reason){

        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setStatus(Status.REJECTED);
        article.setUpdatedAt(LocalDateTime.now());

        return "Article rejected";
    }


    public Page<ArticleCardDto> getPendingArticles(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return articleRepo.findByStatusWithAuthor(Status.PENDING, pageable)
                .map(this::mapToCardDto);
    }

    @Transactional
    public String deleteArticleById(Long id){

        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // 🔐 (Optional but recommended) Ownership check
        // String username = auth.getName();
        // if (!article.getAuthor().getUsername().equals(username)) {
        //     throw new RuntimeException("You are not allowed to delete this article");
        // }

        // 🔥 1. Delete MAIN image from Cloudinary
        if (article.getMainImagePublicId() != null) {
            imageUploadService.deleteImage(article.getMainImagePublicId());
        }

        // 🔥 2. Delete GALLERY images from Cloudinary
        if (article.getImages() != null) {
            for (ArticleImage img : article.getImages()) {
                if (img.getPublicId() != null) {
                    imageUploadService.deleteImage(img.getPublicId());
                }
            }
        }

        // 🔥 3. Delete article (cascade handles DB cleanup)
        articleRepo.delete(article);

        return "Article deleted successfully";
    }

    public ArticleDetailDto getArticleById(Long id){

        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        return mapToDetailDto(article);
    }


    // methods for Comment

    @Transactional
    public CommentResponseDto addComment(Long articleId, CommentDto dto, Authentication auth){

        String username = auth.getName();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);

        Comment saved = commentRepo.save(comment);

        return mapToCommentDto(saved);
    }

    public Page<CommentResponseDto> getCommentsByArticleId(Long articleId, int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return commentRepo.findByArticle_Id(articleId,pageable)
                .map(this::mapToCommentDto);
    }

}
