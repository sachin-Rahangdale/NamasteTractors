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
    public String createAccount(UserCreateDto userCreateDto) {

        if (userRepo.findByUsername(userCreateDto.getUsername()).isPresent()) {

            throw new RuntimeException("Email Already Exist");
        }

        User user = new User();

        user.setUsername(userCreateDto.getUsername());

        user.setName(userCreateDto.getName());

        user.setPassword(
                passwordEncoder.encode(userCreateDto.getPassword())
        );

        user.setRole(Role.USER);

        // verification token
        //String token = UUID.randomUUID().toString();

        //user.setEmailVerificationToken(token);

        // OPTIONAL BUT RECOMMENDED
        user.setEnabled(true);

        userRepo.save(user);

        // EMAIL SHOULD NOT BREAK SIGNUP
//        try {
//
//            mailService.sendVerificationMail(
//                    user.getUsername(),
//                    token
//            );
//
//        } catch (Exception e) {
//
//            System.out.println("EMAIL SENDING FAILED");
//
//            e.printStackTrace();
//        }

        return "Account Created, Please Login to continue to site";
    }

    public User login(LoginDto loginDto){

        User user = userRepo.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        if(!user.isEnabled()){
            throw new RuntimeException("Email not verified");
        }

        return user; //
    }



}
