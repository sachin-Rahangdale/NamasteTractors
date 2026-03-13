package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.CreateTractorDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.service.TractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tractor")
public class TractorContrloller {
    @Autowired
    private TractorService tractorService;

    @PostMapping("/create")
    public ResponseEntity<Tractor> createTractor(
            @ModelAttribute CreateTractorDto dto
    ) {

        Tractor tractor = tractorService.createTractor(dto);

        return ResponseEntity.ok(tractor);
    }
}
