package com.namastetractors.namaste_tractors_backend.dto.tractordto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TractorSpecDto {

    @Min(value = 1, message = "Cylinder must be at least 1")
    @Max(value = 6, message = "Cylinder cannot exceed 6")
    private int cylinder;

    @Min(value = 500, message = "Engine capacity must be at least 500cc")
    private int engineCapacity;

    @NotBlank(message = "Clutch type is required")
    private String clutch;

    @NotBlank(message = "Steering type is required")
    private String steering;

    @NotBlank(message = "Gearbox is required")
    private String gearbox;

    @NotBlank(message = "Brakes type is required")
    private String brakes;

    @Min(value = 50, message = "Torque must be at least 50 Nm")
    private int torque;

    @Min(value = 0, message = "Backup torque cannot be negative")
    private int backupTorque;

    @NotBlank(message = "PTO HP is required")
    private String ptoHp;

    @NotBlank(message = "PTO options are required")
    private String ptoOptions;

    @NotBlank(message = "Front tyre is required")
    private String frontTyre;

    @NotBlank(message = "Rear tyre is required")
    private String rearTyre;

    @NotBlank(message = "Rear axle is required")
    private String rearAxle;

    @NotBlank(message = "Front axle is required")
    private String frontAxle;

    @NotBlank(message = "Reduction type is required")
    private String reduction;

    @Min(value = 50, message = "Service interval must be at least 50 hours")
    private int serviceInterval;
}
