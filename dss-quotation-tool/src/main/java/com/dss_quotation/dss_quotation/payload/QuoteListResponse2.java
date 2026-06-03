package com.dss_quotation.dss_quotation.payload;

import com.dss_quotation.dss_quotation.models.QuoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteListResponse2 {

    private Long id;

    private String quoteName;
    private String customerName;
    private QuoteStatus status;

    private Long machineId;
    private String machineName;

//    private Long materialId;
//    private String materialName;

    private int itemCount;
    private int totalQuantity;
    private double totalWeight;
    private double totalMaterialCost;
    private double operationCost;
    private double totalTime;
    private double calculatedPrice;
    private double totalPrice;
    private boolean finalPriceOverridden;
    private double minimumCharge;
    private boolean minCharged;

    private LocalDateTime createdAt;

    // getters and setters
}

