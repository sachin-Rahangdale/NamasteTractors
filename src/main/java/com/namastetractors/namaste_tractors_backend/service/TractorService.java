package com.namastetractors.namaste_tractors_backend.service;

import com.namastetractors.namaste_tractors_backend.dto.CreateTractorDto;
import com.namastetractors.namaste_tractors_backend.dto.TractorSpecDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Brand;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Tractor;
import com.namastetractors.namaste_tractors_backend.entity.tractor.TractorSpecification;
import com.namastetractors.namaste_tractors_backend.repositroy.BrandRepo;
import com.namastetractors.namaste_tractors_backend.repositroy.TractorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TractorService {
    @Autowired
    private TractorRepo tractorRepo;
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private ImageUploadService imageUploadService;

    public Tractor createTractor(CreateTractorDto dto) {

        // Upload image
        String imageUrl = imageUploadService.uploadImage(dto.getImage());

        // Fetch brand
        Brand brand = brandRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        // Create tractor
        Tractor tractor = new Tractor();
        tractor.setModel(dto.getModel());
        tractor.setHp(dto.getHp());
        tractor.setPrice(dto.getPrice());
        tractor.setImageUrl(imageUrl);
        tractor.setBrand(brand);

        // Create specification
        TractorSpecification spec = new TractorSpecification();

        TractorSpecDto specDto = dto.getTractorSpecDto();

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

        // Link tractor and specification
        spec.setTractor(tractor);
        tractor.setSpecification(spec);

        // Save tractor
        return tractorRepo.save(tractor);
    }

}
