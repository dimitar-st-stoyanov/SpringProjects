package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;


@Entity
@Table(
        name = "vendors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_vendor_code", columnNames = "vendor_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "vendor_code", nullable = false, length = 50)
    private String vendorCode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "vat_number", length = 50)
    private String vatNumber;

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "currency", length = 3)
    private String currency; // ISO 4217

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_terms", length = 20)
    private PaymentTerms paymentTerms;

    @Column(name = "active", nullable = false)
    private boolean active;
}
