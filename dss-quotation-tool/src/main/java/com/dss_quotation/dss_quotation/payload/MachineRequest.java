package com.dss_quotation.dss_quotation.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MachineRequest {

    @NotBlank(message = "Machine name is required")
    private String name;

    @NotNull(message = "Power is required")
    @Positive(message = "Power must be greater than zero")
    private Double power;

    @NotNull(message = "Rate per hour is required")
    @Positive(message = "Rate per hour must be greater than zero")
    private Double ratePerHour;

    @NotNull(message = "Efficiency factor is required")
    @Positive(message = "Efficiency factor must be greater than zero")
    private Double efficiencyFactor;


    @Positive(message = "Minimum charge must be greater than zero")
    private double minimumCharge;
}
