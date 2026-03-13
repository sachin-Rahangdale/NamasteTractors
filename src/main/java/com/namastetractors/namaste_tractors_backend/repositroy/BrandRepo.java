package com.namastetractors.namaste_tractors_backend.repositroy;

import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends JpaRepository<Brand,Long> {

}
