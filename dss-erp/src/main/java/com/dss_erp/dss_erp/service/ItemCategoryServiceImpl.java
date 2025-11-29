package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.exceptions.APIException;
import com.dss_erp.dss_erp.exceptions.ResourceNotFoundException;
import com.dss_erp.dss_erp.models.ItemCategory;
import com.dss_erp.dss_erp.payload.ItemCategoryDTO;
import com.dss_erp.dss_erp.payload.ItemCategoryResponse;
import com.dss_erp.dss_erp.repositories.ItemCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private ItemCategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ItemCategoryResponse getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);

        Page<ItemCategory> categoryPage = categoryRepository.findAll(pageDetails);

        List<ItemCategory> allCategories = categoryPage.getContent();
        if(allCategories.isEmpty()){
            throw new APIException("There are no categories!");
        }

        List<ItemCategoryDTO> categoryDTOS = allCategories.stream()
                .map(category -> modelMapper.map(category, ItemCategoryDTO.class))
                .toList();

        ItemCategoryResponse categoryResponse = new ItemCategoryResponse();

        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;

    }

    @Override
    public ItemCategoryDTO createCategory(ItemCategoryDTO categoryDTO) {
        ItemCategory category = modelMapper.map(categoryDTO, ItemCategory.class);
        category.setItemCategoryId(null);
        ItemCategory categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb !=null){
            throw new APIException("Category with the name " + category.getCategoryName() + " already exist!" );
        }
        ItemCategory savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, ItemCategoryDTO.class);
    }

    @Override
    public ItemCategoryDTO deleteCategory(Long categoryId) {
        ItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));

        ItemCategoryDTO deletedCategoryDTO = modelMapper.map(category, ItemCategoryDTO.class);
        categoryRepository.delete(category);
        //return "Category with category ID: " + categoryId + " deleted successfully";
        return deletedCategoryDTO;
    }

    @Override
    public ItemCategoryDTO updateCategory(ItemCategoryDTO categoryDTO, Long categoryId) {
        ItemCategory category = modelMapper.map(categoryDTO, ItemCategory.class);

        ItemCategory savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() ->new ResourceNotFoundException("Category","categoryId", categoryId));
        category.setItemCategoryId(categoryId);

        savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, ItemCategoryDTO.class);
    }
}
