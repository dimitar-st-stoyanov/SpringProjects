package com.ecomm.project.service;

import com.ecomm.project.exceptions.APIException;
import com.ecomm.project.exceptions.ResourceNotFoundException;
import com.ecomm.project.model.Category;
import com.ecomm.project.model.Product;
import com.ecomm.project.payload.ProductDTO;
import com.ecomm.project.payload.ProductResponse;
import com.ecomm.project.repositories.CategoryRepository;
import com.ecomm.project.repositories.ProductRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRespository productRespository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRespository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);

        Page<Product> prodductsPage = productRespository.findAll(pageDetails);

        List<Product> allProducts = prodductsPage.getContent();
        if(allProducts.isEmpty()){
            throw new APIException("There are no products!");
        }

        List<ProductDTO> productDTO = allProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));


        List<Product> productsByCategory = productRespository.findByCategoryOrderByPriceAsc(category);


        List<ProductDTO> productDTO = productsByCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyoword(String keyword) {

        List<Product> productsByKeyword = productRespository.findByProductNameLikeIgnoreCase('%' + keyword + '%');


        List<ProductDTO> productDTO = productsByKeyword.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {
        //Get the existing product from DB
        Product existingProduct = productRespository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId", productId));
         //Update
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        existingProduct.setSpecialPrice(product.getSpecialPrice());


        //Save
        Product savedProduct = productRespository.save(existingProduct);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product deletedProduct = productRespository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));

        ProductDTO deletedProductDTO = modelMapper.map(deletedProduct, ProductDTO.class);

        productRespository.delete(deletedProduct);

        return deletedProductDTO;
    }
}
