package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarPieceDTO {
    private Long id;
    private Double originalLength;
    private Double remainingLength;
    private Boolean isScrap;
    private String remark;
}
