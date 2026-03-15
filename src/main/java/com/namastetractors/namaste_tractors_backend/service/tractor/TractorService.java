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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public List<TractorCardDto> getAllTractors(){
        List<Tractor> tractors = tractorRepo.findAll();
        return tractors.stream().map(tractor -> {
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
    }






}
