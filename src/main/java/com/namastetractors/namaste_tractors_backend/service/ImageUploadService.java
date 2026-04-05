package com.namastetractors.namaste_tractors_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.namastetractors.namaste_tractors_backend.dto.ImageUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageUploadService {
    @Autowired
    private Cloudinary cloudinary;

    public ImageUploadResult uploadImage(MultipartFile file){

        try {
            Map uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            String url = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();

            return new ImageUploadResult(url, publicId);

        } catch(Exception e){
            throw new RuntimeException("Image Upload Failed", e);
        }
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Image delete failed", e);
        }
    }
}
