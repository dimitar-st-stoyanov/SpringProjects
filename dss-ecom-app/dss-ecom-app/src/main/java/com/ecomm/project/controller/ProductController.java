package com.ecomm.project.controller;

import com.ecomm.project.config.AppConfig;
import com.ecomm.project.config.AppConstants;
import com.ecomm.project.model.Product;
import com.ecomm.project.payload.ProductDTO;
import com.ecomm.project.payload.ProductResponse;
import com.ecomm.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    public ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId){

       ProductDTO savedProductDTO =  productService.addProduct(productDTO,categoryId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct(
            @RequestParam(name="keyword", required=false) String keyword,
            @RequestParam(name="category", required = false) String category,
            @RequestParam (name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                         @RequestParam (name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                         @RequestParam (name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                         @RequestParam (name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
                                                         ){

        ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder,keyword,category);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam (name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                                 @RequestParam (name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                 @RequestParam (name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam (name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        ProductResponse productResponse = productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam (name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                                @RequestParam (name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                @RequestParam (name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam (name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){

    ProductResponse productResponse = productService.searchProductByKeyoword(keyword,pageNumber,pageSize,sortBy,sortOrder);
    return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                    @PathVariable Long productId){

        ProductDTO updatedProductDTO = productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProduct = productService.deleteProduct(productId);

        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("Image")MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

}
