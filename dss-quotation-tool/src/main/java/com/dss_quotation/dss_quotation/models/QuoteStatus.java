package com.dss_quotation.dss_quotation.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum QuoteStatus {
    DRAFT,
    SENT,
    ACCEPTED;

    @JsonCreator
    public static QuoteStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.trim().toUpperCase();
        if ("SEND".equals(normalized)) {
            return SENT;
        }

        return QuoteStatus.valueOf(normalized);
    }
}
