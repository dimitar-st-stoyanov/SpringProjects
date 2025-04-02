package com.eccomerce.sb_ecomm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eccomerce.sb_ecomm.model.Category;
import com.eccomerce.sb_ecomm.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

	private long nextId = 1L;
	//private List<Category> categories = new ArrayList();
	@Autowired 
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Category> getAllCategories() {
	
		return categoryRepository.findAll();
	}
	

	@Override
	public void createCategory(Category category) {
		//category.setCategoryId(nextId++);
		categoryRepository.save(category);
		
	}


	@Override
	public String deleteCategory(Long categoryId) {
		
		/*impl List to Stream ; filter checks every catId if its equal to neededId and findFirst,
		  if there no such as Id throw http 404 error notFound.
		 */
		Category category = categoryRepository.findById(categoryId)
					.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));
		
		
		categoryRepository.delete(category);
		
		return "Category with categoryId: " + categoryId + " is deleted!";
	}
	
	
	@Transactional
	@Override
	public Category updateCategory(Category category, Long categoryId) {
	
	 Category savedCategory = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource Not Found"));
	
	category.setCategoryId(categoryId);
	savedCategory = categoryRepository.save(category);
	return savedCategory;
	
			
	}

	
	
}
