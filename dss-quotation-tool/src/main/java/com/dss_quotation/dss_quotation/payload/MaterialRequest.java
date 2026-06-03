package com.dss_quotation.dss_quotation.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MaterialRequest {

    @NotBlank(message = "Material name is required")
    private String name;

    @NotBlank(message = "Material type is required")
    private String type;

    @NotNull(message = "Density is required")
    @Positive(message = "Density must be greater than zero")
    private Double density;

    @NotNull(message = "Price per kg is required")
    @Positive(message = "Price per kg must be greater than zero")
    private Double pricePerKg;
}
