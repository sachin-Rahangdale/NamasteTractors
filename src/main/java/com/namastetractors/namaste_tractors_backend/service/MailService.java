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
public void sendVerificationMail(String email, String token) {

    // LOCALHOST FOR LOCAL TESTING
    String link =
            "https://namastetractors.onrender.com/api/user/verify?token=" + token;

    try {

        SimpleMailMessage message = new SimpleMailMessage();

        // IMPORTANT
        message.setFrom("yourgmail@gmail.com");

        message.setTo(email);

        message.setSubject("Verify Your Email - Namaste Tractor");

        message.setText(
                "Welcome to Namaste Tractor 🚜\n\n" +
                        "Click the link below to verify your account:\n\n" +
                        link +
                        "\n\n" +
                        "If you did not create this account, please ignore this email."
        );

        javaMailSender.send(message);

        System.out.println("Verification email sent successfully");

    } catch (Exception e) {

        System.out.println("EMAIL SENDING FAILED");

        e.printStackTrace();
    }
}
}
