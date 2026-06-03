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
public class QuoteDxfItemResponse {

    private Long id;
    private String fileName;
    private String dxfDownloadUrl;

    private Long materialId;
    private String materialName;
    private double materialPrice;

    private String partName;
    private int quantity;
    private double thickness;
    private int bends;


    private double cutLength;
    private int pierceCount;
    private double width;
    private double height;

    private double weight;
    private double materialCost;

    private double cutTime;
    private double pierceTime;
    private double bendTime;
    private double totalTime;

    private double price;
    private double cost;
    private double profit;
    private double cuttingCost;
    private double bendingCost;
    private double operationCost;
    private List<QuoteItemOperationResponse> operations;
}
