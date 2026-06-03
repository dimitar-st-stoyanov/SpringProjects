package com.dss_erp.dss_erp.models;

public record ComponentKey(
        BomComponentType componentType,
        Long componentId
) {}
