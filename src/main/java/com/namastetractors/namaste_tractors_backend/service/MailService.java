package com.namastetractors.namaste_tractors_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
//verifying the email address before creating an account so that account must be correct
    public void sendVerificationMail(String email,String token){
        String link = "http://localhost:8080/api/user/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify Your Email");
        message.setText("Click the Link to Verify your Account:  "+ link);
        javaMailSender.send(message);
    }
}
