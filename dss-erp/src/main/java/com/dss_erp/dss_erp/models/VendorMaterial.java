package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "vendor_materials",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vendor_component",
                        columnNames = {"vendor_id", "component_type", "component_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorMaterial {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // -------------------------
    // Vendor
    // -------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    // -------------------------
    // Polymorphic component reference
    // -------------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, length = 50)
    private BomComponentType componentType;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    // -------------------------
    // Vendor-specific data
    // -------------------------

    @Column(name = "vendor_material_code", length = 100)
    private String vendorMaterialCode;

    @Column(name = "vendor_material_name", length = 255)
    private String vendorMaterialName;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

    @Column(name = "minimum_order_quantity")
    private Integer minimumOrderQuantity;

    @Column(name = "order_multiple")
    private Integer orderMultiple;

    @Column(name = "preferred", nullable = false)
    private boolean preferred;

    @Column(name = "active", nullable = false)
    private boolean active;
}
