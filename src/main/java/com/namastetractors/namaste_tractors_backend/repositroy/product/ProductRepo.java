package com.namastetractors.namaste_tractors_backend.repositroy.product;

import com.namastetractors.namaste_tractors_backend.emun.Category;
import com.namastetractors.namaste_tractors_backend.entity.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepo extends JpaRepository<Product, Long > {
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByUserUsername(String username, Pageable pageable);

    Page<Product> findByCityIgnoreCase(String city, Pageable pageable);

    @Query("""
    SELECT p FROM Product p
    WHERE (:category IS NULL OR p.category = :category)
      AND (:city IS NULL OR p.city = :city)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
""")
    Page<Product> filterProducts(
            Category category,
            String city,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );

}
