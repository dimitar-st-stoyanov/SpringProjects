package com.dss_erp.dss_erp.controller;


import com.dss_erp.dss_erp.config.AppConstants;
import com.dss_erp.dss_erp.payload.ItemCategoryDTO;
import com.dss_erp.dss_erp.payload.ItemCategoryResponse;
import com.dss_erp.dss_erp.service.ItemCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemCategoryController {

    private ItemCategoryService categoryService;


    public ItemCategoryController(ItemCategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/api/public/categories")
    public ResponseEntity<ItemCategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        ItemCategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<ItemCategoryDTO> createCategory(@Valid @RequestBody ItemCategoryDTO categoryDTO) {
        ItemCategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<ItemCategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        ItemCategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);

    }

    @PutMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<ItemCategoryDTO> updateCategory(@Valid @RequestBody ItemCategoryDTO categoryDTO, @PathVariable Long categoryId) {
        ItemCategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);

    }
}
