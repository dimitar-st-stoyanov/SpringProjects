package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "work_center")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "rate_per_hour", nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerHour;
}