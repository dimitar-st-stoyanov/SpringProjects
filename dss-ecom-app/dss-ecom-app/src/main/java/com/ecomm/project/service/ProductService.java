package com.ecomm.project.service;

import com.ecomm.project.model.Product;
import com.ecomm.project.payload.ProductDTO;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);
}
