package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "warehouse_categories")
@Data
@NoArgsConstructor
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Long itemCategoryId;

    @Column(name = "categoryName")
    @NotBlank
    @Size(min = 5, message = "Category name must contain atleast 5 characters")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<PurchasedItem> purchasedItems;


}