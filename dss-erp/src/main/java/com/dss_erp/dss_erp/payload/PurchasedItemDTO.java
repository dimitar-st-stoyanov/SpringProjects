package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedItemDTO {

    private Long itemId;        // Optional for createRouting, used for update
    private String itemName;    // Can be generated automatically by frontend
    private String description;
    private String image;
    private Integer quantity;
    private String unit;

    private Long categoryId;    // Needed to assign to a category

    // New structured attributes (JSON)
    private Map<String, Object> attributes;
}
