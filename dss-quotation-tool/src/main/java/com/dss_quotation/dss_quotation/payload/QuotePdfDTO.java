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
public class QuotePdfDTO {

    private Long id;
    private String quoteNumber;
    private String quoteName;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String quoteDate;
    private String dueDate;
    private String validUntil;
    private String machineName;
    private String status;
    private int totalQuantity;

    private List<QuotePdfItemDTO> items;

    private String subtotal;
    private String calculatedPrice;
    private String minimumCharge;
    private boolean minCharged;
    private boolean finalPriceOverridden;
    private String tax;
    private String total;
    private String materialCost;
    private String cuttingCost;
    private String bendingCost;
    private String operationCost;
    private String totalCost;
    private String profit;
    private int margin;
    private String totalWeight;
    private String totalTime;
}
