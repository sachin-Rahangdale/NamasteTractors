package com.namastetractors.namaste_tractors_backend.repositroy.tractor;

import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<Image,Long> {
    List<Image> findByTractorId(Long id);

    Optional<Image> findByTractorIdAndImageType(Long tractorId, String imageType);

    void deleteByTractorId(Long id);
}
