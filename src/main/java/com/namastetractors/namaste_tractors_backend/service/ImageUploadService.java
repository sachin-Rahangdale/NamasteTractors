package com.namastetractors.namaste_tractors_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageUploadService {
    @Autowired
    private Cloudinary cloudinary;

    public String UploadImage(MultipartFile file){
        try{
            Map uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();

        }catch (Exception e){
            throw new RuntimeException("Image Upload Failed");
        }
    }
}
