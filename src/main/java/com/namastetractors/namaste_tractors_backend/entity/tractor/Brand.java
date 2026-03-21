package com.namastetractors.namaste_tractors_backend.entity.tractor;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
