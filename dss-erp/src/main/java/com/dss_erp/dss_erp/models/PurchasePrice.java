package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "purchase_price",
        indexes = {
                @Index(name = "idx_price_vendor_material", columnList = "vendor_material_id"),
                @Index(name = "idx_price_validity", columnList = "valid_from, valid_to")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasePrice {

    @Id
    @GeneratedValue
    private UUID id;

    // -------------------------
    // Relations
    // -------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_material_id", nullable = false)
    private VendorMaterial vendorMaterial;

    // -------------------------
    // Price data
    // -------------------------

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal price;

    @Column(nullable = false, length = 3)
    private String currency; // ISO 4217 (EUR, USD, etc.)

    @Enumerated(EnumType.STRING)
    @Column(name = "price_source", nullable = false)
    private PriceSourceType priceSource;

    // -------------------------
    // Validity
    // -------------------------

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    // -------------------------
    // Commercial context
    // -------------------------

    @Column(name = "reference_number")
    private String referenceNumber; // quotation no / PO no

    @Column(name = "minimum_order_quantity")
    private Integer minimumOrderQuantity;

    @Column(nullable = false)
    private boolean active = true;
}