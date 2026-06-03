package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuoteRequest {

    private String quoteName;
    private String customerName;
    private Integer margin;
    private Long machineId;
    private Double minimumCharge;
    private Double finalPrice;
    private List<UpdateQuoteItemRequest> items;
}
