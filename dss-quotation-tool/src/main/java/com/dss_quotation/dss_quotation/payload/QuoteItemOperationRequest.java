package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteItemOperationRequest {

    private Long operationId;
    private Double timeMinutes;
    private Double overrideCost;
    private boolean isOverriden;
}
