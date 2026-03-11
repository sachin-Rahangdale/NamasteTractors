package com.namastetractors.namaste_tractors_backend.controller;

import com.namastetractors.namaste_tractors_backend.dto.LoginDto;
import com.namastetractors.namaste_tractors_backend.dto.UserCreateDto;
import com.namastetractors.namaste_tractors_backend.entity.User;
import com.namastetractors.namaste_tractors_backend.repositroy.UserRepo;
import com.namastetractors.namaste_tractors_backend.service.JwtService;
import com.namastetractors.namaste_tractors_backend.service.MailService;
import com.namastetractors.namaste_tractors_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
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

//    @PostMapping("/login")
//    public String loginUser(@RequestBody LoginDto loginDto){
//        userService.Login(loginDto);
//        return " You have Logged in Seccessfully";
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){

        User user = userRepo.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        if(!user.isEnabled()){
            throw new RuntimeException("Email not verified");
        }

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(token);
    }

}
