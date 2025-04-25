package com.ecomm.project.service;

import com.ecomm.project.model.Product;
import com.ecomm.project.payload.ProductDTO;
import com.ecomm.project.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder);
}
