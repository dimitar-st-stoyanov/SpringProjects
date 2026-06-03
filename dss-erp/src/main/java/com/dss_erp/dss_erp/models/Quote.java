package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ===============================
       RELATIONS
    =============================== */
    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    /* ===============================
       INPUTS
    =============================== */
    private String partName;
    private String material;
    private double thickness;
    private int bends;

    /* ===============================
       GEOMETRY
    =============================== */
    private double cutLength;
    private int pierceCount;
    private double width;
    private double height;

    /* ===============================
       MATERIAL
    =============================== */
    private double weight;
    private double materialCost;

    /* ===============================
       TIME
    =============================== */
    private double cutTime;
    private double pierceTime;
    private double bendTime;
    private double totalTime;

    /* ===============================
       FINAL
    =============================== */
    private double price;

    private LocalDateTime createdAt;
}
