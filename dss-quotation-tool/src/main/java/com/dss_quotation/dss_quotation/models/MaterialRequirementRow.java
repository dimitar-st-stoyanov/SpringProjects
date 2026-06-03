package com.dss_quotation.dss_quotation.models;

import java.math.BigDecimal;

public interface MaterialRequirementRow {
    Long getComponentId();
    String getComponentType();
    BigDecimal getTotalQuantity();
}

