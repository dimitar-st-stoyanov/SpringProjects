package com.dss_quotation.dss_quotation.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<QuoteDxfItem> items = new ArrayList<>();

    /* ===============================
       INPUTS
    =============================== */
    private String quoteName;
    private String customerName;
    private int margin;
    private double minimumCharge;
    @Enumerated(EnumType.STRING)
    private QuoteStatus status;

    private LocalDateTime createdAt;

    /* ===============================
       TOTALS
    =============================== */
    private double totalWeight;
    private double totalMaterialCost;
    private double totalTime;
    private double calculatedPrice;
    private double totalPrice;
    private int totalQuantity;
    private double cost;
    private double profit;
    private double cuttingCost;
    private double bendingCost;
    private double operationCost;
    private boolean minCharged;
    private boolean finalPriceOverridden;

}
