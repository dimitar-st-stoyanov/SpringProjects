package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialResponse {

    private Long id;
    private String name;
    private String type;
    private double density;
    private double pricePerKg;
    private boolean active;
}
