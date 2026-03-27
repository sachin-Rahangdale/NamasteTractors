package com.namastetractors.namaste_tractors_backend.controller.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.CreateBrandDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.service.tractor.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // ================= CREATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Brand> createBrand(
            @Valid @RequestBody CreateBrandDto dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(brandService.createBrand(dto));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    // ================= GET BY ID =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(
            @PathVariable @Positive Long id
    ){
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    // ================= UPDATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrandById(

            @PathVariable @Positive Long id,

            @RequestParam
            @NotBlank(message = "Brand name cannot be empty")
            String name
    ){
        return ResponseEntity.ok(brandService.updateBrandById(id, name));
    }

    // ================= DELETE =================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrandById(
            @PathVariable @Positive Long id
    ){
        return ResponseEntity.ok(brandService.deleteBrandById(id));
    }
}