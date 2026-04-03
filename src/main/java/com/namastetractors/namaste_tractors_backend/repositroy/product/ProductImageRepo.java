package com.namastetractors.namaste_tractors_backend.repositroy.product;

import com.namastetractors.namaste_tractors_backend.entity.products.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepo extends JpaRepository<ProductImage , Long> {

    Optional<ProductImage> findFirstByProductId(Long productId);

    List<ProductImage> findByProductId(Long productId);
}
