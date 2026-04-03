package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.productDto.CreateProductDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductCardDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductDetailDto;
import com.namastetractors.namaste_tractors_backend.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 🔥 1️⃣ CREATE PRODUCT (Only Logged-in Users)
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody CreateProductDto dto
    ){
        ProductCardDto res = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 🔥 2️⃣ ADD IMAGES (Only Logged-in Users)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/images")
    public ResponseEntity<?> addImages(
            @PathVariable Long id,
            @RequestBody List<String> imageUrls
    ){
        productService.addProductImages(id, imageUrls);
        return ResponseEntity.ok("Images added successfully");
    }

    // 🔥 3️⃣ GET ALL PRODUCTS (PUBLIC)
    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        List<ProductCardDto> res = productService.getAllProducts();
        return ResponseEntity.ok(res);
    }

    // 🔥 4️⃣ GET PRODUCT DETAIL (PUBLIC)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        ProductDetailDto res = productService.getProductById(id);
        return ResponseEntity.ok(res);
    }
}
