package com.namastetractors.namaste_tractors_backend.service.product;

import com.namastetractors.namaste_tractors_backend.dto.productDto.CreateProductDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductCardDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductDetailDto;
import com.namastetractors.namaste_tractors_backend.entity.products.Product;
import com.namastetractors.namaste_tractors_backend.entity.products.ProductImage;
import com.namastetractors.namaste_tractors_backend.repositroy.product.ProductImageRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.product.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductImageRepo productImageRepo;

    public ProductService(ProductRepo productRepo, ProductImageRepo productImageRepo) {
        this.productRepo = productRepo;
        this.productImageRepo = productImageRepo;
    }

    // 🔥 1️⃣ CREATE PRODUCT
    @Transactional
    public ProductCardDto createProduct(CreateProductDto dto) {

        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setUnit(dto.getUnit());
        product.setPhone(dto.getPhone());
        product.setCity(dto.getCity());
        product.setPincode(dto.getPincode());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setCreatedAt(LocalDateTime.now());

        Product saved = productRepo.save(product);

        return mapToCardDto(saved, null); // no image yet
    }

    // 🔥 2️⃣ ADD IMAGES
    @Transactional
    public void addProductImages(Long productId, List<String> imageUrls) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductImage> images = imageUrls.stream().map(url -> {
            ProductImage img = new ProductImage();
            img.setImageUrl(url);
            img.setProduct(product);
            return img;
        }).toList();

        productImageRepo.saveAll(images);
    }

    // 🔥 3️⃣ GET ALL PRODUCTS (CARD)
    public List<ProductCardDto> getAllProducts() {

        List<Product> products = productRepo.findAll();

        return products.stream().map(product -> {

            // 🔥 get first image
            String imageUrl = productImageRepo
                    .findFirstByProductId(product.getId())
                    .map(ProductImage::getImageUrl)
                    .orElse(null);

            return mapToCardDto(product, imageUrl);

        }).toList();
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


