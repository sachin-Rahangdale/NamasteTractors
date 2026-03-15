package com.namastetractors.namaste_tractors_backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/hello")
    public String hello(){
        return "JWT Authentication Working!";
    }


    @PostMapping("/upload")
    public String testUpload(@RequestParam("image") MultipartFile file) throws Exception {

        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());

        return uploadResult.get("secure_url").toString();
    }

}
