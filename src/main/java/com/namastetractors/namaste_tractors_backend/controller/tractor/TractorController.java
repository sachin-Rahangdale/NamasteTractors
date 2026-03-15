package com.namastetractors.namaste_tractors_backend.controller.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.CreateTractorDto;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorCardDto;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorImageDto;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorResponseDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.service.tractor.ImageService;
import com.namastetractors.namaste_tractors_backend.service.tractor.TractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tractor")
public class TractorController {
    @Autowired
    private TractorService tractorService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/create")
    public Tractor createTractor(@RequestBody CreateTractorDto dto){

        return tractorService.createTractor(dto);
    }

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> createImage(
            @RequestPart("image") MultipartFile image,
            @RequestPart("type") String type,
            @RequestParam Long tractorId) {

        TractorImageDto dto = new TractorImageDto();
        dto.setImage(image);
        dto.setType(type);
        dto.setTractorId(tractorId);

        Image savedImage = imageService.createImage(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TractorResponseDto> getTractorById(@PathVariable Long id){

        return ResponseEntity.ok(tractorService.getTractorById(id));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<TractorCardDto>> getAllTractors(){

        return ResponseEntity.ok(tractorService.getAllTractors());
    }

}
