package com.dss_erp.dss_erp.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponse {

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
}
