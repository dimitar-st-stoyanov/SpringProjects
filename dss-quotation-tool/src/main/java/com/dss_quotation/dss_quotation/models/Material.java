package com.dss_quotation.dss_quotation.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;          // e.g. Steel S235

    private String type;          // steel / stainless / aluminum

    private double density;       // kg/m3

    private double pricePerKg;    // €/kg

    private boolean active;       // enable/disable
}
