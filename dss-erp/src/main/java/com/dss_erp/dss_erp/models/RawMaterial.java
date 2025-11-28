package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class RawMaterial {

    // Common fields
    private String materialType; // e.g. Sheet, Tube, Rod, etc.
    private String grade;        // e.g. AISI 304, EN 1.4301
    private String standard;
    private String unit;         // kg, piece, m, etc.
    private Double density;      // kg/m3

    private String description;  // optional free text

    private Double weight;       // auto-calculated total weight
    private String name;         // auto-generated name
}
