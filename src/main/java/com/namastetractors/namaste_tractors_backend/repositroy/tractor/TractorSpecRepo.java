package com.namastetractors.namaste_tractors_backend.repositroy.tractor;

import com.namastetractors.namaste_tractors_backend.entity.tractor.TractorSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TractorSpecRepo extends JpaRepository<TractorSpecification , Long> {

    TractorSpecification findByTractorId(Long id);

    void deleteByTractorId(Long id);
}
