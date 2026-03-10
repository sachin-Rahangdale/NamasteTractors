package com.namastetractors.namaste_tractors_backend.entity;

import com.namastetractors.namaste_tractors_backend.emun.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = false;

    private String emailVerificationToken;



}
