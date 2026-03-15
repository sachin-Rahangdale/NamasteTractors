package com.namastetractors.namaste_tractors_backend.entity.tractor;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tractor_specs")
public class TractorSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tractorId;

    private int cylinder;

    private int engineCapacity;

    private String clutch;

    private String steering;

    private String gearbox;

    private String brakes;

    private int torque;

    private int backupTorque;

    private String ptoHp;

    private String ptoOptions;

    private String frontTyre;

    private String rearTyre;

    private String rearAxle;

    private String frontAxle;

    private String reduction;

    private int serviceInterval;





}
