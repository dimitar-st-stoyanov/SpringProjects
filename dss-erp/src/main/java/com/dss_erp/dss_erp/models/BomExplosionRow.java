package com.dss_erp.dss_erp.models;

import java.math.BigDecimal;

public interface BomExplosionRow {

        Long getComponentId();
        String getComponentType();
        BigDecimal getQuantity();
        Integer getLevel();

}
