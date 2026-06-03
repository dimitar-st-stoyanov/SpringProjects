package com.dss_erp.dss_erp.payload;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkCenterDTO {

    private Long id;
    private String name;
    private BigDecimal ratePerHour;
}