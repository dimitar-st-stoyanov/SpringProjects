package com.dss_quotation.dss_quotation.payload;


import com.dss_quotation.dss_quotation.models.QuoteStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteDetailsResponse {

    private Long id;

    private Long machineId;
    private String machineName;

//    private Long materialId;
//    private String materialName;
//    private double materialPrice;

    private String quoteName;
    private String customerName;
    private QuoteStatus status;

    private double totalWeight;
    private double totalMaterialCost;
    private double cuttingCost;
    private double bendingCost;
    private double operationCost;
    private double totalTime;

    private double calculatedPrice;
    private double totalPrice;
    private boolean finalPriceOverridden;
    private int totalQuantity;
    private double cost;
    private double profit;
    private int margin;
    private double minimumCharge;
    private boolean minCharged;

    private LocalDateTime createdAt;

    private List<QuoteDxfItemResponse> items;

}
