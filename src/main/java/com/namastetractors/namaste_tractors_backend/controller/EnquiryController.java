package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.EnquiryRequestDto;
import com.namastetractors.namaste_tractors_backend.dto.EnquiryResponseDto;
import com.namastetractors.namaste_tractors_backend.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    // ✅ PUBLIC (no login required)
    @PostMapping
    public ResponseEntity<?> createEnquiry(
            @Valid @RequestBody EnquiryRequestDto dto
    ){
        EnquiryResponseDto res = enquiryService.createEnquiry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // ✅ ADMIN ONLY → view all enquiries
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllEnquiries(){
        List<EnquiryResponseDto> res = enquiryService.getAllEnquiries();
        return ResponseEntity.ok(res);
    }

    // ✅ ADMIN ONLY → update status
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateEnquiryStatus(
            @PathVariable Long id,
            @RequestParam String progress
    ){
        EnquiryResponseDto res = enquiryService.updateEnquiry(id, progress);
        return ResponseEntity.ok(res);
    }

    // ✅ ADMIN ONLY → delete enquiry
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnquiry(@PathVariable Long id){
        enquiryService.deleteEnquiryById(id);
        return ResponseEntity.ok("Enquiry deleted successfully");
    }
}
