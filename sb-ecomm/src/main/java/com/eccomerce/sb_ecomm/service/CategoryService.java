package com.eccomerce.sb_ecomm.service;

import java.util.List;

import com.eccomerce.sb_ecomm.model.Category;

public interface CategoryService {
	
	List<Category> getAllCategories();
	void createCategory(Category category);
	String deleteCategory(Long categoryId);
	Category updateCategory(Category category, Long categoryId);

}
