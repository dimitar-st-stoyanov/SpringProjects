package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Product;
import com.dss_erp.dss_erp.payload.ProductDTO;
import com.dss_erp.dss_erp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        if (productRepository.existsByCode(dto.getCode())) {
            throw new IllegalStateException("Product code already exists");
        }
        Product p = new Product();
        p.setCode(dto.getCode());
        p.setName(dto.getName());
        p.setLevel(dto.getLevel());
        p.setUnit(dto.getUnit());
        p.setActive(dto.getActive() != null ? dto.getActive() : true);
        Product saved = productRepository.save(p);
        return modelMapper.map(saved, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
    }
}
