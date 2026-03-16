package com.namastetractors.namaste_tractors_backend.repositroy.tractor;

import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TractorRepo extends JpaRepository<Tractor,Long> {
    List<Tractor> findByBrandId(Long brandId);
    @Query("""
SELECT t FROM Tractor t
WHERE (:brandId IS NULL OR t.brand.id = :brandId)
AND (:minHp IS NULL OR t.hp >= :minHp)
AND (:maxHp IS NULL OR t.hp <= :maxHp)
AND (:minPrice IS NULL OR t.price >= :minPrice)
AND (:maxPrice IS NULL OR t.price <= :maxPrice)
""")
    List<Tractor> filterTractors(
            Long brandId,
            Integer minHp,
            Integer maxHp,
            Double minPrice,
            Double maxPrice
    );
}
