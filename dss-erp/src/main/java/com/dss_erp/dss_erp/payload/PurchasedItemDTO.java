package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedItemDTO {
    private Long itemId;
    private String itemName;
    private String description;
    private String image;
    private Integer quantity;



}
