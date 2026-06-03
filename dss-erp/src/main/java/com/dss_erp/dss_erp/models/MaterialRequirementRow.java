package com.dss_erp.dss_erp.models;

import java.math.BigDecimal;

public interface MaterialRequirementRow {
    Long getComponentId();
    String getComponentType();
    BigDecimal getTotalQuantity();
}

