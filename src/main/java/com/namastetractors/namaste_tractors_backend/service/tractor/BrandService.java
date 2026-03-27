package com.namastetractors.namaste_tractors_backend.service.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.CreateBrandDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.BrandRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {

    private final BrandRepo brandRepo;

    // ================= CREATE =================
    public Brand createBrand(CreateBrandDto dto){

        Brand brand = new Brand();
        brand.setName(dto.getName());

        return brandRepo.save(brand);
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<Brand> getAllBrands(){
        return brandRepo.findAll();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public Brand getBrandById(Long id){
        return findBrandOrThrow(id);
    }

    // ================= UPDATE =================
    public Brand updateBrandById(Long id, String name){

        Brand brand = findBrandOrThrow(id);
        brand.setName(name);

        return brandRepo.save(brand);
    }

    // ================= DELETE =================
    public String deleteBrandById(Long id){

        Brand brand = findBrandOrThrow(id);
        brandRepo.delete(brand);

        return "Brand deleted successfully";
    }

    // ================= HELPER =================
    private Brand findBrandOrThrow(Long id){
        return brandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
    }
}