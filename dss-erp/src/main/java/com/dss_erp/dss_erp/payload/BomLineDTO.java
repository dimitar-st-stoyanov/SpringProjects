package com.dss_erp.dss_erp.payload;
import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.models.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BomLineDTO {

    private Long id;

    private BomComponentType componentType; // NEW
    private Long componentId; // NEW

    private Double quantity;
    private UnitOfMeasure unit;
}