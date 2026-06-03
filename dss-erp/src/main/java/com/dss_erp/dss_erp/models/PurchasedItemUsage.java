package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchased_items_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedItemUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long purchasedItemId;
    private Integer quantityUsed;
    private String usedFor;
    private LocalDateTime usedAt = LocalDateTime.now();
}
