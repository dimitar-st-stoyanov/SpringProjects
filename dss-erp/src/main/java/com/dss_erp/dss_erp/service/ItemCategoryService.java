package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.ItemCategoryDTO;
import com.dss_erp.dss_erp.payload.ItemCategoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface ItemCategoryService {

    ItemCategoryResponse getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ItemCategoryDTO createCategory(ItemCategoryDTO categoryDTO);

    ItemCategoryDTO deleteCategory(Long categoryId);

    ItemCategoryDTO updateCategory(ItemCategoryDTO categoryDTO, Long categoryId);
}
