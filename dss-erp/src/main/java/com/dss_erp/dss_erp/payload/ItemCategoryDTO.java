package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryDTO {
    private long itemCategoryId;
    private String categoryName;
    private Map<String, Object> attributes;
}
