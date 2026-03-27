package com.namastetractors.namaste_tractors_backend.service.tractor;
import org.springframework.transaction.annotation.Transactional;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.*;
import com.namastetractors.namaste_tractors_backend.emun.ImageType;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.entity.tractor.TractorSpecification;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.BrandRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.TractorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TractorService {

    private final TractorRepo tractorRepo;
    private final BrandRepo brandRepo;

    // ================= CREATE =================
    public Tractor createTractor(CreateTractorDto dto){

        Brand brand = brandRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        // 🔥 CLEAN INPUT
        String model = dto.getModel().trim().replaceAll("\\s+", " ");

        // 🔥 CHECK DUPLICATE (BETTER)
        boolean exists = tractorRepo.existsByModelIgnoreCaseAndBrand_Id(
                model,
                dto.getBrandId()
        );

        if (exists){
            throw new RuntimeException("This model already exists for this brand");
        }

        Tractor tractor = new Tractor();
        tractor.setModel(model);
        tractor.setHp(dto.getHp());
        tractor.setPrice(dto.getPrice());
        tractor.setBrand(brand);

        // 🔥 SPECIFICATION
        TractorSpecification spec = new TractorSpecification();
        mapSpecDtoToEntity(dto.getSpecification(), spec);
        spec.setTractor(tractor);

        tractor.setSpecification(spec);

        return tractorRepo.save(tractor);
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public TractorResponseDto getTractorById(Long id){

        Tractor tractor = tractorRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Tractor not found"));

        return mapToResponseDto(tractor);
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<TractorCardDto> getAllTractors(int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<Tractor> tractorPage = tractorRepo.findAllWithBrand(pageable);

        return tractorPage.getContent()
                .stream()
                .map(this::mapToCardDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getTractorByBrand(Long brandId, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<Tractor> tractorPage = tractorRepo.findByBrand_Id(brandId, pageable);

        List<TractorCardDto> list = tractorPage.getContent()
                .stream()
                .map(this::mapToCardDto)
                .toList();

        return Map.of(
                "content", list,
                "page", tractorPage.getNumber(),
                "totalPages", tractorPage.getTotalPages(),
                "totalElements", tractorPage.getTotalElements()
        );
    }

    // ================= DELETE =================
    public String deleteTractorById(Long id){

        Tractor tractor = tractorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tractor not found"));

        tractorRepo.delete(tractor); // 🔥 cascade handles everything

        return "Tractor Deleted Successfully";
    }

    // ================= UPDATE =================
    public Tractor updateTractorById(Long id, CreateTractorDto dto){

        Tractor tractor = tractorRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Tractor not found"));

        Brand brand = brandRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        tractor.setBrand(brand);
        tractor.setHp(dto.getHp());
        tractor.setModel(dto.getModel());
        tractor.setPrice(dto.getPrice());

        mapSpecDtoToEntity(dto.getSpecification(), tractor.getSpecification());

        return tractorRepo.save(tractor);
    }

    // ================= FILTER =================
    @Transactional(readOnly = true)
    public Map<String, Object> filterTractors(
            Long brandId,
            Integer minHp,
            Integer maxHp,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size
    ){

        Pageable pageable = PageRequest.of(page, size);

        Page<Tractor> tractorPage = tractorRepo.filterTractors(
                brandId, minHp, maxHp, minPrice, maxPrice, pageable
        );

        List<TractorCardDto> list = tractorPage.getContent()
                .stream()
                .map(this::mapToCardDto)
                .toList();

        return Map.of(
                "content", list,
                "page", tractorPage.getNumber(),
                "totalPages", tractorPage.getTotalPages(),
                "totalElements", tractorPage.getTotalElements()
        );
    }

    // ================= MAPPERS =================

    private TractorCardDto mapToCardDto(Tractor tractor){

        TractorCardDto dto = new TractorCardDto();

        dto.setId(tractor.getId());
        dto.setModel(tractor.getModel());
        dto.setHp(tractor.getHp());
        dto.setPrice(tractor.getPrice());
        dto.setBrand(tractor.getBrand().getName());

        String mainImage = tractor.getImages()
                .stream()
                .filter(img -> img.getImageType() == ImageType.MAIN)
                .map(Image::getImageUrl)
                .findFirst()
                .orElse(null);

        dto.setImageUrl(mainImage);

        return dto;
    }

    private TractorResponseDto mapToResponseDto(Tractor tractor){

        TractorResponseDto dto = new TractorResponseDto();

        dto.setId(tractor.getId());
        dto.setModel(tractor.getModel());
        dto.setHp(tractor.getHp());
        dto.setPrice(tractor.getPrice());
        dto.setBrand(tractor.getBrand().getName());

        // spec
        TractorSpecDto specDto = new TractorSpecDto();
        mapSpecEntityToDto(tractor.getSpecification(), specDto);
        dto.setSpecification(specDto);

        // images
        List<ImageResponseDto> images = tractor.getImages()
                .stream()
                .map(img -> {
                    ImageResponseDto i = new ImageResponseDto();
                    i.setImageUrl(img.getImageUrl());
                    i.setImageType(img.getImageType());
                    return i;
                }).toList();

        dto.setImages(images);

        return dto;
    }

    private void mapSpecDtoToEntity(TractorSpecDto dto, TractorSpecification spec){

        spec.setCylinder(dto.getCylinder());
        spec.setEngineCapacity(dto.getEngineCapacity());
        spec.setClutch(dto.getClutch());
        spec.setSteering(dto.getSteering());
        spec.setGearbox(dto.getGearbox());
        spec.setBrakes(dto.getBrakes());
        spec.setTorque(dto.getTorque());
        spec.setBackupTorque(dto.getBackupTorque());
        spec.setPtoHp(dto.getPtoHp());
        spec.setPtoOptions(dto.getPtoOptions());
        spec.setFrontTyre(dto.getFrontTyre());
        spec.setRearTyre(dto.getRearTyre());
        spec.setRearAxle(dto.getRearAxle());
        spec.setFrontAxle(dto.getFrontAxle());
        spec.setReduction(dto.getReduction());
        spec.setServiceInterval(dto.getServiceInterval());
    }

    private void mapSpecEntityToDto(TractorSpecification spec, TractorSpecDto dto){

        dto.setCylinder(spec.getCylinder());
        dto.setEngineCapacity(spec.getEngineCapacity());
        dto.setClutch(spec.getClutch());
        dto.setSteering(spec.getSteering());
        dto.setGearbox(spec.getGearbox());
        dto.setBrakes(spec.getBrakes());
        dto.setTorque(spec.getTorque());
        dto.setBackupTorque(spec.getBackupTorque());
        dto.setPtoHp(spec.getPtoHp());
        dto.setPtoOptions(spec.getPtoOptions());
        dto.setFrontTyre(spec.getFrontTyre());
        dto.setRearTyre(spec.getRearTyre());
        dto.setRearAxle(spec.getRearAxle());
        dto.setFrontAxle(spec.getFrontAxle());
        dto.setReduction(spec.getReduction());
        dto.setServiceInterval(spec.getServiceInterval());
    }
}
