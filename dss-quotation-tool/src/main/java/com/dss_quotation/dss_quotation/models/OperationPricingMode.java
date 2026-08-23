package com.dss_quotation.dss_quotation.models;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum OperationPricingMode {
    FIXED,
    PER_QUANTITY,
    PER_HOUR,
    PER_BEND;

    @JsonCreator
    public static OperationPricingMode from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return OperationPricingMode.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new APIException("Unsupported operation pricing mode: " + value);
        }
    }

    public boolean requiresTime() {
        return this == PER_HOUR;
    }
}
