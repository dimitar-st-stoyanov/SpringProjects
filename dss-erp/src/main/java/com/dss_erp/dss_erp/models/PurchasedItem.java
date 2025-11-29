package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchased_items")
@ToString
public class PurchasedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    @NotBlank
    @Size(min = 3, message = "Item name must contain atleast 3 characters" )
    private String itemName;
    private String image;
    @NotBlank
    @Size(min = 6 ,message = "Item description must contain atleast 6 characters" )
    private String description;
    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "category_id")
    ItemCategory category;

//    @ManyToOne
//    @JoinColumn(name = "vendor_id")
//    private Vendor vendor;
    
    
    
}
