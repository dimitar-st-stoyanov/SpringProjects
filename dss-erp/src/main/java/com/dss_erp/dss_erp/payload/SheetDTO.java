package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetDTO {
    private Long id;
    private String grade;
    private String unit;
    private String standard;
    private Double density;
    private Double thickness;
    private Double width;
    private Double length;
    private Integer quantity;
    private Double weight; // auto-calculated
    private String name;   // auto-generated
}
