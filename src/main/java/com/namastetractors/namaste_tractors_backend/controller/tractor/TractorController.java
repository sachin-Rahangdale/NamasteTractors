package com.namastetractors.namaste_tractors_backend.controller.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.*;
import com.namastetractors.namaste_tractors_backend.emun.ImageType;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.service.tractor.ImageService;
import com.namastetractors.namaste_tractors_backend.service.tractor.TractorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/tractors")
@RequiredArgsConstructor // 🔥 constructor injection
public class TractorController {

    private final TractorService tractorService;
    private final ImageService imageService;

    // ================= CREATE TRACTOR =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TractorResponseDto> createTractor(
            @Valid @RequestBody CreateTractorDto dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tractorService.getTractorById(
                        tractorService.createTractor(dto).getId()
                ));
    }

    // ================= UPLOAD IMAGE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponseDto> createImage(

            @PathVariable Long id,

            @RequestParam("image")
            MultipartFile image,

            @RequestParam("type")
            ImageType type
    ) {

        if (image.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        TractorImageDto dto = new TractorImageDto();
        dto.setImage(image);
        dto.setType(type);
        dto.setTractorId(id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.createImage(dto));
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<TractorResponseDto> getTractorById(
            @PathVariable @Positive Long id
    ){
        return ResponseEntity.ok(tractorService.getTractorById(id));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<TractorCardDto>> getAllTractors(

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1)
            @Max(value = 50)
            int size
    ){
        return ResponseEntity.ok(tractorService.getAllTractors(page, size));
    }


    // ================= UPDATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TractorResponseDto> updateTractorById(

            @PathVariable @Positive Long id,

            @Valid @RequestBody CreateTractorDto dto
    ){
        tractorService.updateTractorById(id, dto);
        return ResponseEntity.ok(tractorService.getTractorById(id));
    }

    // ================= DELETE =================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTractorById(
            @PathVariable @Positive Long id
    ){
        return ResponseEntity.ok(tractorService.deleteTractorById(id));
    }

    // ================= GET BY BRAND =================
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<Map<String, Object>> getTractorByBrand(

            @PathVariable @Positive Long brandId,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(tractorService.getTractorByBrand(brandId, page, size));
    }

    // ================= FILTER =================
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterTractors(

            @RequestParam(required = false) Long brandId,

            @RequestParam(required = false)
            @Min(0)
            Integer minHp,

            @RequestParam(required = false)
            @Min(0)
            Integer maxHp,

            @RequestParam(required = false)
            @DecimalMin("0.0")
            BigDecimal minPrice,

            @RequestParam(required = false)
            @DecimalMin("0.0")
            BigDecimal maxPrice,

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "10")
            @Min(1)
            @Max(50)
            int size
    ){
        return ResponseEntity.ok(
                tractorService.filterTractors(
                        brandId,
                        minHp,
                        maxHp,
                        minPrice,
                        maxPrice,
                        page,
                        size
                )
        );
    }
}