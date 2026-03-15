package com.namastetractors.namaste_tractors_backend.service.tractor;
import com.namastetractors.namaste_tractors_backend.dto.tractordto.TractorImageDto;
import com.namastetractors.namaste_tractors_backend.entity.tractor.Image;
import com.namastetractors.namaste_tractors_backend.repositroy.tractor.ImageRepo;
import com.namastetractors.namaste_tractors_backend.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepo imageRepo;
    @Autowired
    private ImageUploadService imageUploadService;

    public Image createImage(TractorImageDto dto) {

        String imageUrl = imageUploadService.uploadImage(dto.getImage());

        Image image = new Image();
        image.setImageType(dto.getType());
        image.setImageUrl(imageUrl);
        image.setTractorId(dto.getTractorId());

        return imageRepo.save(image);
    }
}
