package com.namastetractors.namaste_tractors_backend.service.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.CreateBrandDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.BrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    private BrandRepo brandRepo;

    public Brand createBrand(CreateBrandDto dto){

        Brand brand = new Brand();
        brand.setName(dto.getName());

        return brandRepo.save(brand);
    }


    public List<Brand> getAllBrands(){
        return brandRepo.findAll();
    }


    public Brand getBrandById(Long id){
        return brandRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(" Brand with specific Id does not exists"));
    }


    public Brand updateBrandById(Long id, String name){
        Brand br = brandRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Brand Does not Exists"));
        br.setName(name);
        return brandRepo.save(br);
    }


    public String deleteBrandById(Long id){
        Brand br = brandRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(" Brand with specific Id does not exists"));
        brandRepo.delete(br);
        return " Brand deleted successfully";
    }
}
