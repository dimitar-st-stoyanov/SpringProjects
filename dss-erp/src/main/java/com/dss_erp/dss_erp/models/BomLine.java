package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "bom_lines",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_bom_component",
                columnNames = {"bom_id", "component_type", "component_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BomLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BOM version this line belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false)
    private Bom bom;

    // Type of component (PRODUCT, SHEET, BAR, etc.)
    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false)
    private BomComponentType componentType;

    // ID of the component in the corresponding table
    @Column(name = "component_id", nullable = false)
    private Long componentId;

    private Double quantity;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unit;

    @Column(name = "frozen_unit_price")
    private Double frozenUnitPrice;

    @Column(name = "frozen_currency")
    private String frozenCurrency;
}
