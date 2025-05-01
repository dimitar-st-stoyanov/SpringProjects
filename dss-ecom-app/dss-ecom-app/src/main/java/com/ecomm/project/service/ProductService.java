package com.ecomm.project.service;

import com.ecomm.project.payload.ProductDTO;
import com.ecomm.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Long categoryId);

    ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductByKeyoword(String keyword,int pageNumber,int pageSize,String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productId, ProductDTO product);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
