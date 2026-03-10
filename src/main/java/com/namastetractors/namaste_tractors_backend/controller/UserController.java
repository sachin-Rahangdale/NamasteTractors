package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.LoginDto;
import com.namastetractors.namaste_tractors_backend.dto.UserCreateDto;
import com.namastetractors.namaste_tractors_backend.entity.User;
import com.namastetractors.namaste_tractors_backend.repositroy.UserRepo;
import com.namastetractors.namaste_tractors_backend.service.MailService;
import com.namastetractors.namaste_tractors_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody UserCreateDto userCreateDto){
        return ResponseEntity.ok(userService.createAccount(userCreateDto));
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token){
        User user = userRepo.findByEmailVerificationToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid Token"));
        user.setEnabled(true);
        user.setEmailVerificationToken(null);
        userRepo.save(user);
        return "Account Verified Succesfully";

    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginDto loginDto){
        userService.Login(loginDto);
        return " You have Logged in Seccessfully";
    }
}
