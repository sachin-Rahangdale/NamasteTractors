package com.namastetractors.namaste_tractors_backend.repositroy.tractor;

import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TractorRepo extends JpaRepository<Tractor,Long> {
    @Query("""
SELECT t FROM Tractor t
LEFT JOIN FETCH t.images
LEFT JOIN FETCH t.specification
JOIN FETCH t.brand
WHERE t.id = :id
""")
    Optional<Tractor> findByIdWithDetails(Long id);


    @Query("""
SELECT t FROM Tractor t
JOIN FETCH t.brand
""")
    Page<Tractor> findAllWithBrand(Pageable pageable);


    @Query("""
SELECT t FROM Tractor t
WHERE (:brandId IS NULL OR t.brand.id = :brandId)
AND (:minHp IS NULL OR t.hp >= :minHp)
AND (:maxHp IS NULL OR t.hp <= :maxHp)
AND (:minPrice IS NULL OR t.price >= :minPrice)
AND (:maxPrice IS NULL OR t.price <= :maxPrice)
""")
    Page<Tractor> filterTractors(
            Long brandId,
            Integer minHp,
            Integer maxHp,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Tractor> findByBrand_Id(Long brandId, Pageable pageable);

    boolean existsByModelIgnoreCaseAndBrand_Id(String model, Long brandId);
}
