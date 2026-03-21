package com.namastetractors.namaste_tractors_backend.service.tractor;

import com.namastetractors.namaste_tractors_backend.dto.tractordto.*;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.entity.tractor.TractorSpecification;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.BrandRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.ImageRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.TractorRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.TractorSpecRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TractorService {
    @Autowired
    private TractorRepo tractorRepo;
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private TractorSpecRepo tractorSpecRepo;
    @Autowired
    private ImageRepo imageRepo;


    public Tractor createTractor(CreateTractorDto dto){

        Brand brand = brandRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        if(dto.getSpecification() == null){
            throw new RuntimeException("Specification is required");
        }

        Tractor tractor = new Tractor();
        tractor.setModel(dto.getModel());
        tractor.setHp(dto.getHp());
        tractor.setPrice(dto.getPrice());
        tractor.setBrand(brand);

        Tractor savedTractor = tractorRepo.save(tractor);

        TractorSpecDto specDto = dto.getSpecification();

        TractorSpecification spec = new TractorSpecification();

        spec.setTractorId(savedTractor.getId());
        spec.setCylinder(specDto.getCylinder());
        spec.setEngineCapacity(specDto.getEngineCapacity());
        spec.setClutch(specDto.getClutch());
        spec.setSteering(specDto.getSteering());
        spec.setGearbox(specDto.getGearbox());
        spec.setBrakes(specDto.getBrakes());
        spec.setTorque(specDto.getTorque());
        spec.setBackupTorque(specDto.getBackupTorque());
        spec.setPtoHp(specDto.getPtoHp());
        spec.setPtoOptions(specDto.getPtoOptions());
        spec.setFrontTyre(specDto.getFrontTyre());
        spec.setRearTyre(specDto.getRearTyre());
        spec.setRearAxle(specDto.getRearAxle());
        spec.setFrontAxle(specDto.getFrontAxle());
        spec.setReduction(specDto.getReduction());
        spec.setServiceInterval(specDto.getServiceInterval());

        tractorSpecRepo.save(spec);

        return savedTractor;
    }


    public TractorResponseDto getTractorById(Long id){

        Tractor tractor = tractorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tractor not found"));

        TractorResponseDto response = new TractorResponseDto();

        response.setId(tractor.getId());
        response.setModel(tractor.getModel());
        response.setHp(tractor.getHp());
        response.setPrice(tractor.getPrice());
        response.setBrand(tractor.getBrand().getName());

        // specification
        TractorSpecification spec = tractorSpecRepo.findByTractorId(id);

        if(spec == null){
            throw new RuntimeException("Specification not found");
        }

        TractorSpecDto specDto = new TractorSpecDto();

        specDto.setCylinder(spec.getCylinder());
        specDto.setEngineCapacity(spec.getEngineCapacity());
        specDto.setClutch(spec.getClutch());
        specDto.setSteering(spec.getSteering());
        specDto.setGearbox(spec.getGearbox());
        specDto.setBrakes(spec.getBrakes());
        specDto.setTorque(spec.getTorque());
        specDto.setBackupTorque(spec.getBackupTorque());
        specDto.setPtoHp(spec.getPtoHp());
        specDto.setPtoOptions(spec.getPtoOptions());
        specDto.setFrontTyre(spec.getFrontTyre());
        specDto.setRearTyre(spec.getRearTyre());

        response.setSpecification(specDto);

        // images
        List<Image> images = imageRepo.findByTractorId(id);

        List<ImageResponseDto> imageResponses = images.stream().map(img -> {

            ImageResponseDto i = new ImageResponseDto();
            i.setImageUrl(img.getImageUrl());
            i.setImageType(img.getImageType());
            return i;

        }).toList();

        response.setImages(imageResponses);

        return response;
    }


    public List<TractorCardDto> getAllTractors(int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<Tractor> tractorPage = tractorRepo.findAll(pageable);

        return tractorPage.getContent().stream().map(tractor -> {

            TractorCardDto dto = new TractorCardDto();
            dto.setId(tractor.getId());
            dto.setModel(tractor.getModel());
            dto.setHp(tractor.getHp());
            dto.setBrand(tractor.getBrand().getName());
            dto.setPrice(tractor.getPrice());

            String imgUrl = imageRepo
                    .findByTractorIdAndImageType(tractor.getId(), "FRONT")
                    .map(Image::getImageUrl)
                    .orElse(null);

            dto.setImageUrl(imgUrl);

            return dto;

        }).toList();
    }

    @Transactional
    public String deleteTractorById(Long id){
        Tractor tractor = tractorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tractor not found"));
        imageRepo.deleteByTractorId(id);
        tractorSpecRepo.deleteByTractorId(id);
        tractorRepo.deleteById(id);

        return " Tractor Deleted Succesfully";
    }

    public Tractor updateTractorById(Long tracId, CreateTractorDto dto){

        Tractor tractor = tractorRepo.findById(tracId)
                .orElseThrow(() -> new RuntimeException("Tractor doesn't exist"));

        Brand brand = brandRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        TractorSpecification tracSpec = tractorSpecRepo.findByTractorId(tracId);

        if(tracSpec == null){
            throw new RuntimeException("Specification not found");
        }

        // update tractor
        tractor.setBrand(brand);
        tractor.setHp(dto.getHp());
        tractor.setModel(dto.getModel());
        tractor.setPrice(dto.getPrice());

        // update specification
        tracSpec.setBrakes(dto.getSpecification().getBrakes());
        tracSpec.setClutch(dto.getSpecification().getClutch());
        tracSpec.setCylinder(dto.getSpecification().getCylinder());
        tracSpec.setGearbox(dto.getSpecification().getGearbox());
        tracSpec.setBackupTorque(dto.getSpecification().getBackupTorque());
        tracSpec.setFrontAxle(dto.getSpecification().getFrontAxle());
        tracSpec.setReduction(dto.getSpecification().getReduction());
        tracSpec.setTorque(dto.getSpecification().getTorque());
        tracSpec.setSteering(dto.getSpecification().getSteering());
        tracSpec.setServiceInterval(dto.getSpecification().getServiceInterval());
        tracSpec.setPtoHp(dto.getSpecification().getPtoHp());
        tracSpec.setPtoOptions(dto.getSpecification().getPtoOptions());
        tracSpec.setRearTyre(dto.getSpecification().getRearTyre());
        tracSpec.setFrontTyre(dto.getSpecification().getFrontTyre());
        tracSpec.setEngineCapacity(dto.getSpecification().getEngineCapacity());

        tractorSpecRepo.save(tracSpec);

        return tractorRepo.save(tractor);
    }

    public Map<String, Object> getTractorByBrand(Long brandId, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<Tractor> tractorPage = tractorRepo.findByBrand_Id(brandId, pageable);

        List<TractorCardDto> list = tractorPage.getContent().stream().map(tractor -> {

            TractorCardDto dto = new TractorCardDto();

            dto.setId(tractor.getId());
            dto.setModel(tractor.getModel());
            dto.setHp(tractor.getHp());
            dto.setBrand(tractor.getBrand().getName());
            dto.setPrice(tractor.getPrice());

            String imgUrl = imageRepo
                    .findByTractorIdAndImageType(tractor.getId(),"FRONT")
                    .map(Image::getImageUrl)
                    .orElse(null);

            dto.setImageUrl(imgUrl);

            return dto;

        }).toList();

        return Map.of(
                "content", list,
                "page", tractorPage.getNumber(),
                "totalPages", tractorPage.getTotalPages(),
                "totalElements", tractorPage.getTotalElements()
        );
    }
    //Applied Filer to get tractors

    public Map<String, Object> filterTractors(
            Long brandId,
            Integer minHp,
            Integer maxHp,
            Double minPrice,
            Double maxPrice,
            int page,
            int size
    ){

        Pageable pageable = PageRequest.of(page, size);

        Page<Tractor> tractorPage = tractorRepo.filterTractors(
                brandId,
                minHp,
                maxHp,
                minPrice,
                maxPrice,
                pageable
        );

        List<TractorCardDto> list = tractorPage.getContent().stream().map(tractor -> {

            TractorCardDto dto = new TractorCardDto();

            dto.setId(tractor.getId());
            dto.setModel(tractor.getModel());
            dto.setHp(tractor.getHp());
            dto.setPrice(tractor.getPrice());
            dto.setBrand(tractor.getBrand().getName());

            String imgUrl = imageRepo
                    .findByTractorIdAndImageType(tractor.getId(),"FRONT")
                    .map(Image::getImageUrl)
                    .orElse(null);

            dto.setImageUrl(imgUrl);

            return dto;

        }).toList();

        return Map.of(
                "content", list,
                "page", tractorPage.getNumber(),
                "totalPages", tractorPage.getTotalPages(),
                "totalElements", tractorPage.getTotalElements()
        );
    }
}
