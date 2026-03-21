package com.namastetractors.namaste_tractors_backend.service.article;

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

        // 1️⃣ Extract username from JWT
        String username = auth.getName();

        // 2️⃣ Fetch user from DB
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Validate image
        if(mainImage == null || mainImage.isEmpty()){
            throw new RuntimeException("Main image is required");
        }

        // 4️⃣ Upload image (Cloudinary)
        String imageUrl = imageUploadService.uploadImage(mainImage);

        // 5️⃣ Generate slug
        String slug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");

        // 6️⃣ Create article
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setMainImageUrl(imageUrl);
        article.setSlug(slug);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setAuthor(user);

        // 👉 IMPORTANT (moderation system)
        if(user.getRole() == Role.ADMIN){
            article.setStatus(Status.APPROVED);
        } else {
            article.setStatus(Status.PENDING);
        }

        // 7️⃣ Save
        Article saved = articleRepo.save(article);

        // 8️⃣ Map to DTO
        ArticleCardDto res = new ArticleCardDto();
        res.setId(saved.getId());
        res.setTitle(saved.getTitle());
        res.setSlug(saved.getSlug());
        res.setMainImageUrl(saved.getMainImageUrl());
        res.setAuthor(
                user.getName() != null
                        ? user.getName()
                        : user.getUsername().split("@")[0]
        );
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

    public Page<ArticleCardDto> getAllArticles(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Article> articlePage = articleRepo.findByStatus(Status.APPROVED, pageable);

        return articlePage.map(article -> {

            ArticleCardDto dto = new ArticleCardDto();

            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            dto.setSlug(article.getSlug());
            dto.setMainImageUrl(article.getMainImageUrl());
            dto.setAuthor(article.getAuthor().getUsername());
            dto.setCreatedAt(article.getCreatedAt());

            return dto;
        });
    }

    public ArticleDetailDto getArticleBySlug(String slug){

        Article article = articleRepo.findBySlugAndStatus(slug, Status.APPROVED)
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

    public String approveArticle(Long id){

        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if(article.getStatus() == Status.APPROVED){
            return "Article already approved";
        }

        article.setStatus(Status.APPROVED);
        article.setUpdatedAt(LocalDateTime.now());

        articleRepo.save(article);

        return "Article approved successfully";
    }

    public String rejectArticle(Long id, String reason){

        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setStatus(Status.REJECTED);
        article.setUpdatedAt(LocalDateTime.now());


        articleRepo.save(article);

        return "Article rejected";
    }


    public Page<ArticleCardDto> getPendingArticles(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Article> articlePage = articleRepo.findByStatus(Status.PENDING, pageable);

        return articlePage.map(article -> {

            ArticleCardDto dto = new ArticleCardDto();

            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            dto.setSlug(article.getSlug());
            dto.setMainImageUrl(article.getMainImageUrl());
            dto.setAuthor(article.getAuthor().getUsername());
            dto.setCreatedAt(article.getCreatedAt());

            return dto;
        });
    }


    @Transactional
    public String deleteArticleById(Long id){

        // 1️⃣ Delete comments of this article
        commentRepo.deleteByArticleId(id);

        // 2️⃣ Delete images of this article
        articleImageRepo.deleteByArticleId(id);

        // 3️⃣ Delete article
        articleRepo.deleteById(id);

        return "Article deleted successfully";
    }

    public ArticleDetailDto getArticleById(Long id){

        Article article = articleRepo.findById(id)
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


    // methods for Comment

    @Transactional
    public CommentResponseDto addComment(Long articleId, CommentDto dto, Authentication auth){

        String username = auth.getName();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user); // 🔥 relation

        Comment saved = commentRepo.save(comment);

        // DTO
        CommentResponseDto res = new CommentResponseDto();
        res.setId(saved.getId());
        res.setContent(saved.getContent());
        res.setName(user.getName()); // 🔥 from user
        res.setCreatedAt(saved.getCreatedAt());

        return res;
    }

    public Page<CommentResponseDto> getCommentsByArticleId(Long articleId, int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Comment> comments = commentRepo.findByArticleId(articleId, pageable);

        return comments.map(comment -> {
            CommentResponseDto dto = new CommentResponseDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());

            // 🔥 name from user
            dto.setName(
                    comment.getUser().getName() != null
                            ? comment.getUser().getName()
                            : comment.getUser().getUsername().split("@")[0]
            );

            dto.setCreatedAt(comment.getCreatedAt());
            return dto;
        });
    }

}
