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
public class QuoteItemOperationResponse {

    private Long id;
    private Long operationId;
    private String name;
    private OperationPricingMode pricingMode;
    private double rate;
    private double timeMinutes;
    private double cost;
    private boolean active;
}
