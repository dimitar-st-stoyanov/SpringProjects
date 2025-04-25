package com.ecomm.project.controller;

import com.ecomm.project.config.AppConfig;
import com.ecomm.project.config.AppConstants;
import com.ecomm.project.model.Product;
import com.ecomm.project.payload.ProductDTO;
import com.ecomm.project.payload.ProductResponse;
import com.ecomm.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    public ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product,
                                                 @PathVariable Long categoryId){

       ProductDTO productDTO =  productService.addProduct(product,categoryId);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct(@RequestParam (name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
                                                         @RequestParam (name="pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize,
                                                         @RequestParam (name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                         @RequestParam (name="sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
                                                         ){

        ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


}
