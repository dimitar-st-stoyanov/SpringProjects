package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteListResponse {

    private Long id;
    private String machineName;

    private String material;
    private double thickness;

    private double price;
    private double totalTime;
    private int totalQuantity;

    private LocalDateTime createdAt;
}
