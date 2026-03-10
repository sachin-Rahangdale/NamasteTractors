package com.namastetractors.namaste_tractors_backend.service;
import com.namastetractors.namaste_tractors_backend.dto.LoginDto;
import com.namastetractors.namaste_tractors_backend.dto.UserCreateDto;
import com.namastetractors.namaste_tractors_backend.emun.Role;
import com.namastetractors.namaste_tractors_backend.entity.User;
import com.namastetractors.namaste_tractors_backend.repositroy.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    //creating an account
    public String createAccount(UserCreateDto userCreateDto){
        if(userRepo.findByUsername(userCreateDto.getUsername()).isPresent()){
            throw new RuntimeException("Username Already Exist");
        }
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRole(Role.USER);
        //generation verification token here
        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);
        userRepo.save(user);
        mailService.sendVerificationMail(user.getUsername(), token);
        return "Account Created, Please Verify Your Email";


    }

    public String Login (LoginDto loginDto){
        User user = userRepo.findByUsername(loginDto.getUsername())
                .orElseThrow(()->new RuntimeException(" UserName Does not Exist"));
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword() )){
            throw  new RuntimeException("Invalid Password");
        }
        if(!user.isEnabled()){
            throw new RuntimeException("Email is not Verified");
        }
        return "Login Successful";
    }




}
