package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.UnitOfMeasure;

import java.math.BigDecimal;

public record BomLineViewDTO(
        Long id,
        String componentType,
        Long componentId,
        Long componentCode,
        String componentName,
        Double quantity,
        UnitOfMeasure unit,
        BigDecimal price,
        String currency
) {}
