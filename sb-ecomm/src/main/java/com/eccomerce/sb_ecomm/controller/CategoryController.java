package com.eccomerce.sb_ecomm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eccomerce.sb_ecomm.model.Category;
import com.eccomerce.sb_ecomm.service.CategoryService;
import com.eccomerce.sb_ecomm.service.CategoryServiceImpl;

@RestController
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	


	@GetMapping("api/public/categories")
	public ResponseEntity<List<Category>>getAllCategories() {
		List<Category> categories =  categoryService.getAllCategories();
		return new ResponseEntity<>(categories, HttpStatus.OK);
		
	}
	
	@PostMapping("api/public/categories")
	public ResponseEntity <String> createCategory(@RequestBody Category category) {
		
		categoryService.createCategory(category);
		
		return new ResponseEntity<>("Category created successfully!", HttpStatus.CREATED);
	}
	
	@DeleteMapping("api/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		try {
		String status = categoryService.deleteCategory(categoryId);
		// if its delete successfull get message from service layer and return https status ok
		return new ResponseEntity<>(status, HttpStatus.OK);
		}
		catch (ResponseStatusException e) {
			// if no catch error and return http status code
			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
		}
		
	}
	
	
	@PutMapping("api/public/categories/{categoryId}")
	public ResponseEntity<String>updateCategory(@RequestBody Category category,
												@PathVariable Long categoryId){
		
		try {
			Category savedCategory= categoryService.updateCategory(category, categoryId);
			
			return new ResponseEntity<>("Category with categoryId: "+ categoryId, HttpStatus.OK);
						
		}catch (ResponseStatusException e) {
			// if no catch error and return http status code
			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
		}
	}

}
