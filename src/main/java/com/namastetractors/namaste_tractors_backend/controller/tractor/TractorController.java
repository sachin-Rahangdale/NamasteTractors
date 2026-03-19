package com.namastetractors.namaste_tractors_backend.controller.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.CreateTractorDto;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorCardDto;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorImageDto;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorResponseDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.service.tractor.ImageService;
import com.namastetractors.namaste_tractors_backend.service.tractor.TractorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping("/api/tractors")
public class TractorController {
    @Autowired
    private TractorService tractorService;

    @Autowired
    private ImageService imageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Tractor createTractor(@Valid @RequestBody CreateTractorDto dto){
        return tractorService.createTractor(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> createImage(

            @RequestPart("image") @NotNull(message = "Image file is required") MultipartFile image,

            @RequestPart("type")
            @NotBlank(message = "Image type is required")
            @Pattern(
                    regexp = "^(MAIN|GALLERY|THUMBNAIL)$",
                    message = "Type must be MAIN, GALLERY or THUMBNAIL"
            )
            String type,

            @RequestParam @NotNull(message = "Tractor ID is required") Long tractorId
    ) {

        // 🔥 extra safety (must for file)
        if (image.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        TractorImageDto dto = new TractorImageDto();
        dto.setImage(image);
        dto.setType(type);
        dto.setTractorId(tractorId);

        Image savedImage = imageService.createImage(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TractorResponseDto> getTractorById(@PathVariable Long id){

        return ResponseEntity.ok(tractorService.getTractorById(id));
    }

    @GetMapping
    public ResponseEntity<List<TractorCardDto>> getAllTractors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(tractorService.getAllTractors(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Tractor> updateTractorById(

            @PathVariable @NotNull(message = "Tractor ID is required") Long id,

            @Valid @RequestBody CreateTractorDto dto
    ){
        return ResponseEntity.ok(tractorService.updateTractorById(id,dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTractorById(

            @PathVariable @NotNull(message = "Tractor ID is required") Long id
    ){
        return ResponseEntity.ok(tractorService.deleteTractorById(id));
    }

    @GetMapping("/{id}/brand")
    public ResponseEntity<Map<String, Object>> getTractorByBrand(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(tractorService.getTractorByBrand(id, page, size));
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterTractors(

            @RequestParam(required = false) Long brandId,

            @RequestParam(required = false)
            @Min(value = 0, message = "minHp cannot be negative")
            Integer minHp,

            @RequestParam(required = false)
            @Min(value = 0, message = "maxHp cannot be negative")
            Integer maxHp,

            @RequestParam(required = false)
            @Min(value = 0, message = "minPrice cannot be negative")
            Double minPrice,

            @RequestParam(required = false)
            @Min(value = 0, message = "maxPrice cannot be negative")
            Double maxPrice,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 50, message = "Size cannot exceed 50")
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
