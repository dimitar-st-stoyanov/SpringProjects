package com.dss_erp.dss_erp.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuoteListResponse {

    private Long id;
    private String machineName;

    private String material;
    private double thickness;

    private double price;
    private double totalTime;

    private LocalDateTime createdAt;
}