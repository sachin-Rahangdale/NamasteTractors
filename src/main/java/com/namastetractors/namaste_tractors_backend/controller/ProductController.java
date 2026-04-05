package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.productDto.CreateProductDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductCardDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.ProductDetailDto;
import com.namastetractors.namaste_tractors_backend.dto.productDto.UpdateProductDto;
import com.namastetractors.namaste_tractors_backend.emun.Category;
import com.namastetractors.namaste_tractors_backend.service.product.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @Valid @RequestBody CreateProductDto dto, Authentication auth
    ){
        ProductCardDto res = productService.createProduct(dto, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 🔥 2️⃣ ADD IMAGES (Only Logged-in Users)
    @PreAuthorize("isAuthenticated()")
    @PostMapping(
            value = "/{id}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> addImages(
            @PathVariable Long id,

            @Parameter(
                    description = "Upload product images (first image will be main)",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                    )
            )
            @RequestPart("images") List<MultipartFile> files,
            Authentication auth
    ) {
        productService.addProductImages(id, files, auth);
        return ResponseEntity.ok("Images uploaded successfully");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            Authentication auth
    ) {
        productService.deleteProduct(id, auth);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // 🔥 3️⃣ GET ALL PRODUCTS (PUBLIC)
    @GetMapping
    public ResponseEntity<Page<ProductCardDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }
    // 🔥 4️⃣ GET PRODUCT DETAIL (PUBLIC)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        ProductDetailDto res = productService.getProductById(id);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ProductCardDto> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductDto dto,
            Authentication auth
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto, auth));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductCardDto>> filterProducts(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                productService.filterProducts(category, city, minPrice, maxPrice, page, size)
        );
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<Page<ProductCardDto>> getProductsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                productService.getProductsByCity(city, page, size)
        );
    }
}
