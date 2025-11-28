package com.example.dss_hellbomreader.util;

public class QuantityParser {

    /**
     * Safe parse quantity string.
     * Handles empty, null, thousands separators, decimal comma.
     * Rounds PC units to integer.
     */
    public static double parseSafe(String quantityStr, String unit) {
        if (quantityStr == null || quantityStr.isBlank()) return 0.0;

        String normalized = quantityStr.trim()
                .replace(".", "")  // remove thousands
                .replace(",", "."); // decimal comma to point

        double value;
        try {
            value = Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            return 0.0;
        }

        if (unit != null && unit.equalsIgnoreCase("PC")) {
            value = Math.round(value);
        }

        return value;
    }
}
