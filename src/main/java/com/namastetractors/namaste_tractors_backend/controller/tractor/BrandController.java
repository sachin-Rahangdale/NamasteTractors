package com.namastetractors.namaste_tractors_backend.controller.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.CreateBrandDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.service.tractor.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Brand createBrand(@Valid @RequestBody CreateBrandDto dto){

        return brandService.createBrand(dto);
    }

    // 🔍 Get Brand by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    // ✏️ Update Brand
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrandById(
            @PathVariable Long id,
            @RequestParam String name
    ) {
        return ResponseEntity.ok(brandService.updateBrandById(id, name));
    }

    // ❌ Delete Brand
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.deleteBrandById(id));
    }
}
