package com.dss_quotation.dss_quotation.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteDxfItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Quote quote;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @OneToMany(mappedBy = "quoteDxfItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<QuoteDxfItemOperation> operations = new ArrayList<>();

    private String fileName;

    @Lob
    @Column(name = "dxf_file")
    private byte[] dxfFile;

    private String partName;
    private int quantity;
    private double thickness;
    private int bends;

    private double cutLength;
    private int pierceCount;
    private double width;
    private double height;

    private double weight;
    private double materialCost;

    private double cutTime;
    private double pierceTime;
    private double bendTime;
    private double totalTime;

    private double price;
    private double cost;
    private double profit;
    private double cuttingCost;
    private double bendingCost;
    private double operationCost;
}
