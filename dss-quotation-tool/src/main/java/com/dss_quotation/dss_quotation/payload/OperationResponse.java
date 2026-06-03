package com.dss_quotation.dss_quotation.payload;

import com.dss_quotation.dss_quotation.models.OperationPricingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationResponse {

    private Long id;
    private String name;
    private OperationPricingMode pricingMode;
    private double rate;
    private boolean active;
}
