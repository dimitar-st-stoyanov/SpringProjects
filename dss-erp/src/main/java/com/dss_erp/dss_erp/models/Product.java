package com.dss_erp.dss_erp.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_code", columnNames = "code")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;   // internal part number
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductLevel level;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unit;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bom> boms = new ArrayList<>();


    private Boolean active = true;
}
