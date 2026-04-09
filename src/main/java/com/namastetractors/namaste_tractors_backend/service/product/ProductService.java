package com.namastetractors.namaste_tractors_backend.service.product;

import com.namastetractors.namaste_tractors_backend.dto.ImageUploadResult;
import com.namastetractors.namaste_tractors_backend.dto.productDto.CreateProductDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductCardDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductDetailDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.UpdateProductDto;
import com.namastetractors.namaste_tractors_backend.emun.Category;
import com.namastetractors.namaste_tractors_backend.emun.Role;
import com.namastetractors.namaste_tractors_backend.entity.User;
import com.namastetractors.namaste_tractors_backend.entity.products.Product;
import com.namastetractors.namaste_tractors_backend.entity.products.ProductImage;
import com.namastetractors.namaste_tractors_backend.repositroy.UserRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.product.ProductImageRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.product.ProductRepo;
import com.namastetractors.namaste_tractors_backend.service.ImageUploadService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductImageRepo productImageRepo;
    private final ImageUploadService imageUploadService;
    private final UserRepo userRepo;

    public ProductService(ProductRepo productRepo,UserRepo userRepo, ProductImageRepo productImageRepo, ImageUploadService imageUploadService) {
        this.productRepo = productRepo;
        this.productImageRepo = productImageRepo;
        this.imageUploadService = imageUploadService;
        this.userRepo=userRepo;
    }

    // 🔥 1️⃣ CREATE PRODUCT
    @Transactional
    public ProductCardDto createProduct(CreateProductDto dto, Authentication auth) {

        String username = auth.getName();
        User user = userRepo.findByUsername(username).orElseThrow(()->new RuntimeException("User Not Found"));



        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setUnit(dto.getUnit());
        product.setPhone(dto.getPhone());
        product.setCity(dto.getCity().toLowerCase());
        product.setPincode(dto.getPincode());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setCreatedAt(LocalDateTime.now());
        product.setUser(user);

        Product saved = productRepo.save(product);

        return mapToCardDto(saved, null); // no image yet
    }

    @Transactional
    public void addProductImages(Long productId, List<MultipartFile> files, Authentication auth) {

        String username = auth.getName();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔐 Ownership check
        if (!product.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to modify this product");
        }

        boolean hasMainImage = productImageRepo
                .existsByProductIdAndIsMainTrue(productId);

        List<ProductImage> images = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            MultipartFile file = files.get(i);

            // 🔥 FIXED: handle ImageUploadResult
            ImageUploadResult result = imageUploadService.uploadImage(file);

            ProductImage img = new ProductImage();
            img.setImageUrl(result.getUrl());        // ✅ URL
            img.setPublicId(result.getPublicId());   // ✅ PUBLIC ID
            img.setProduct(product);

            // ✅ Safe main image logic
            img.setMain(!hasMainImage && i == 0);

            images.add(img);
        }

        productImageRepo.saveAll(images);

        product.getImages().addAll(images); // optional but clean
    }

    // 🔥 3️⃣ GET ALL PRODUCTS (CARD)
    public Page<ProductCardDto> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Product> products = productRepo.findAll(pageable);

        return products.map(product -> {

            String imageUrl = productImageRepo
                    .findFirstByProductId(product.getId())
                    .map(ProductImage::getImageUrl)
                    .orElse(null);

            return mapToCardDto(product, imageUrl);
        });
    }
    // Get PRODUCR BY User

    public Page<ProductCardDto> getMyProducts(Authentication auth, int page, int size) {

        String username = auth.getName();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Product> products = productRepo.findByUserUsername(username, pageable);

        return products.map(product -> {

            String imageUrl = productImageRepo
                    .findFirstByProductId(product.getId())
                    .map(ProductImage::getImageUrl)
                    .orElse(null);

            return mapToCardDto(product, imageUrl);
        });
    }

    // 🔥 4️⃣ GET PRODUCT DETAIL
    public ProductDetailDto getProductById(Long id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<String> imageUrls = productImageRepo
                .findByProductId(product.getId())
                .stream()
                .map(ProductImage::getImageUrl)
                .toList();

        return mapToDetailDto(product, imageUrls);
    }

    @Transactional
    public void deleteProduct(Long productId, Authentication auth) {

        String username = auth.getName();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔥 Get current user
        User currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Ownership OR Admin check
        if (!product.getUser().getUsername().equals(username)
                && currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("You are not allowed to delete this product");
        }

        // ☁️ Delete images from Cloudinary
        if (product.getImages() != null) {
            for (ProductImage img : product.getImages()) {
                if (img.getPublicId() != null) {
                    imageUploadService.deleteImage(img.getPublicId());
                }
            }
        }

        // 🗑 Delete product (DB cleanup via cascade)
        productRepo.delete(product);
    }

    @Transactional
    public ProductCardDto updateProduct(Long id, UpdateProductDto dto, Authentication auth) {

        String username = auth.getName();

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔐 Ownership check
        if (!product.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to update this product");
        }

        // 🔥 Partial update (only if not null)
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getCity() != null) {
            product.setCity(dto.getCity());
        }

        // 🔥 Get main image (optional improvement)
        String imageUrl = productImageRepo
                .findFirstByProductId(product.getId())
                .map(ProductImage::getImageUrl)
                .orElse(null);

        return mapToCardDto(product, imageUrl);
    }


    public Page<ProductCardDto> filterProducts(
            Category category,
            String city,
            Double minPrice,
            Double maxPrice,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Product> products = productRepo.filterProducts(
                category, city, minPrice, maxPrice, pageable
        );

        return products.map(product -> {

            String imageUrl = productImageRepo
                    .findFirstByProductId(product.getId())
                    .map(ProductImage::getImageUrl)
                    .orElse(null);

            return mapToCardDto(product, imageUrl);
        });
    }


    public Page<ProductCardDto> getProductsByCity(String city, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Product> products = productRepo.findByCityIgnoreCase(city, pageable);

        return products.map(product -> {

            String imageUrl = productImageRepo
                    .findFirstByProductId(product.getId())
                    .map(ProductImage::getImageUrl)
                    .orElse(null);

            return mapToCardDto(product, imageUrl);
        });
    }

    // =====================================================
    // 🔥 MAPPERS
    // =====================================================

    private ProductCardDto mapToCardDto(Product product, String imageUrl) {

        ProductCardDto dto = new ProductCardDto();

        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setUnit(product.getUnit());
        dto.setCity(product.getCity());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(imageUrl);
        dto.setCreatedAt(product.getCreatedAt());

        return dto;
    }

    private ProductDetailDto mapToDetailDto(Product product, List<String> imageUrls) {

        ProductDetailDto dto = new ProductDetailDto();

        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setUnit(product.getUnit());
        dto.setPhone(product.getPhone());
        dto.setCity(product.getCity());
        dto.setPincode(product.getPincode());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setImageUrls(imageUrls);

        return dto;
    }
}


