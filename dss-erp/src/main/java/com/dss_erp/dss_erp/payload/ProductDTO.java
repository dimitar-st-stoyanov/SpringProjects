package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.ProductLevel;
import com.dss_erp.dss_erp.models.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String code;   // internal part number
    private String name;
    private ProductLevel level;
    private UnitOfMeasure unit;
    private Boolean active;
}
