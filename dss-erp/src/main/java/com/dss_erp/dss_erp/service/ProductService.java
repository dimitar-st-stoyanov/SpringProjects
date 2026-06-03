package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    public ProductDTO createProduct(ProductDTO dto);
    public List<ProductDTO> getAllProducts();

}
