package com.ecomm.project.service;

import com.ecomm.project.exceptions.APIException;
import com.ecomm.project.exceptions.ResourceNotFoundException;
import com.ecomm.project.model.Category;
import com.ecomm.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        if(allCategories.isEmpty()){
            throw new APIException("There are no categories!");
        }
        return categoryRepository.findAll();

    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory !=null){
            throw new APIException("Category with the name " + category.getCategoryName() + " already exist!" );
        }
        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));


        categoryRepository.delete(category);
        return "Category with category ID: " + categoryId + " deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                        .orElseThrow(() ->new ResourceNotFoundException("Category","categoryId", categoryId));
        category.setCategoryId(categoryId);

        savedCategory = categoryRepository.save(category);

        return savedCategory;
    }
}

