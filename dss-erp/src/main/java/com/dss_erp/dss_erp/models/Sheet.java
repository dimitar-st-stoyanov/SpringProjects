package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sheets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SequenceGenerator(
        name = "sheet_seq",
        sequenceName = "sheet_seq",
        initialValue = 110001,
        allocationSize = 1
)
public class Sheet extends RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sheet_seq")
    private Long id;

    private Double thickness;  // mm
    private Double width;      // mm
    private Double length;     // mm
    private Integer quantity = 0;  // number of sheets

    public void calculateWeight() {
        double safeThickness = (thickness != null) ? thickness / 1000.0 : 0.0;
        double safeWidth = (width != null) ? width / 1000.0 : 0.0;
        double safeLength = (length != null) ? length / 1000.0 : 0.0;
        int safeQuantity = (quantity != null) ? quantity : 0; // default 1 if null
        double safeDensity = (getDensity() != null) ? getDensity() : 0.0;

        double volume = safeThickness * safeWidth * safeLength * safeQuantity;
        setWeight(volume * safeDensity * 1000); // kg
    }

    public void generateName() {
        setName("Sheet " + getGrade() + " " + thickness + "mm " + length.intValue() + "x" + width.intValue());
    }
}
