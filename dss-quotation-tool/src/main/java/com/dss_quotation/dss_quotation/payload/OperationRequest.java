package com.dss_quotation.dss_quotation.payload;

import com.dss_quotation.dss_quotation.models.OperationPricingMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OperationRequest {

    @NotBlank(message = "Operation name is required")
    private String name;

    @NotNull(message = "Pricing mode is required")
    private OperationPricingMode pricingMode;

    @NotNull(message = "Rate is required")
    @Positive(message = "Rate must be greater than zero")
    private Double rate;
}
