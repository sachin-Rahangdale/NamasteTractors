package com.namastetractors.namaste_tractors_backend.controller.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.CreateBrandDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.service.tractor.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @PostMapping
    public Brand createBrand(@Valid @RequestBody CreateBrandDto dto){

        return brandService.createBrand(dto);
    }
}
