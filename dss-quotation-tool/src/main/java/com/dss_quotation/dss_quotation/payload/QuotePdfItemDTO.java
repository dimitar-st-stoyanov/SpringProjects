package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotePdfItemDTO {

    private int qty;
    private int quantity;
    private String partName;
    private String materialName;
    private String thickness;
    private String description;
    private String unitPrice;
    private String amount;
    private String calculatedAmount;
    private String materialCost;
    private String cuttingCost;
    private String bendingCost;
    private String operationCost;
    private String operations;
    private String weight;
    private String cutTime;
    private String pierceTime;
    private String bendTime;
    private String totalTime;
    private int bends;
    private int pierceCount;
    private String cutLength;
}
