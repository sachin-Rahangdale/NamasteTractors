package com.namastetractors.namaste_tractors_backend.service;

import com.namastetractors.namaste_tractors_backend.dto.EnquiryRequestDto;
import com.namastetractors.namaste_tractors_backend.dto.EnquiryResponseDto;
import com.namastetractors.namaste_tractors_backend.entity.Enquiry;
import com.namastetractors.namaste_tractors_backend.repositroy.EnquiryRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnquiryService {

    private final EnquiryRepo enquiryRepo;

    public EnquiryService(EnquiryRepo enquiryRepo) {
        this.enquiryRepo = enquiryRepo;
    }

    @Transactional
    public EnquiryResponseDto createEnquiry(EnquiryRequestDto dto){

        Enquiry enq = new Enquiry();
        enq.setName(dto.getName());
        enq.setPhone(dto.getPhone());
        enq.setEnquiryType(dto.getEnquiryType());
        enq.setMessage(dto.getMessage());
        enq.setCreatedAt(LocalDateTime.now());
        enq.setPincode(dto.getPincode());
        enq.setAddress(dto.getAddress());
        enq.setProgress("NEW");

        Enquiry saved = enquiryRepo.save(enq);

        return mapToResponseDto(saved);
    }

    public List<EnquiryResponseDto> getAllEnquiries(){
        List<Enquiry> enq = enquiryRepo.findAll(Sort.by("createdAt").descending());
        return enq.stream()
                .map(this::mapToResponseDto)
                .toList();
    }
    private EnquiryResponseDto mapToResponseDto(Enquiry enq){

        EnquiryResponseDto dto = new EnquiryResponseDto();

        dto.setId(enq.getId());
        dto.setName(enq.getName());
        dto.setPhone(enq.getPhone());
        dto.setEnquiryType(enq.getEnquiryType());
        dto.setMessage(enq.getMessage());
        dto.setPincode(enq.getPincode());
        dto.setAddress(enq.getAddress());
        dto.setCreatedAt(enq.getCreatedAt());

        return dto;
    }

    public EnquiryResponseDto updateEnquiry(Long enquiryId, String progress){

        Enquiry enquiry = enquiryRepo.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry Id is Incorrect"));

        enquiry.setUpdatedAt(LocalDateTime.now());
        enquiry.setProgress(progress);

        Enquiry saved = enquiryRepo.save(enquiry);

        return mapToResponseDto(saved);
    }

    //closing enquiry
    public String deleteEnquiryById(Long enqId){
        Enquiry enquiry = enquiryRepo.findById(enqId)
                .orElseThrow(()->new RuntimeException("Enquiry Id is Incorrect"));
        enquiryRepo.delete(enquiry);
        return " Enquiry Completed Succesfully";
    }

}
