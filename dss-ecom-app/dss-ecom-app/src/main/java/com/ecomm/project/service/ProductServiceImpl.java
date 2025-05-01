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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRespository productRespository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        //Check if product is already present -> throws product with name already exist
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNoPresent = true;

        List<Product> products = category.getProducts();

        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNoPresent = false;
                break;
            }
        }

        if(isProductNoPresent) {
            Product product = modelMapper.map(productDTO, Product.class);

            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRespository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else{
            throw new APIException("Product already exist!!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);

        Page<Product> productsPage = productRespository.findAll(pageDetails);

        List<Product> allProducts = productsPage.getContent();
        if(allProducts.isEmpty()){
            throw new APIException("There are no products!");
        }

        List<ProductDTO> productDTO = allProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);

        Page<Product> productsPage = productRespository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> productsByCategory = productsPage.getContent();
        if(productsByCategory.isEmpty()){
            throw new APIException("There are no products!");
        }


       // List<Product> productsByCategory = productRespository.findByCategoryOrderByPriceAsc(category);


        List<ProductDTO> productDTO = productsByCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyoword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);

        Page<Product> productsPage = productRespository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Product> productsByKeyword = productsPage.getContent();
        if(productsByKeyword.isEmpty()){
            throw new APIException("There are no products!");
        }
        //List<Product> productsByKeyword = productRespository.findByProductNameLikeIgnoreCase('%' + keyword + '%');


        List<ProductDTO> productDTO = productsByKeyword.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        Product existingProduct = productRespository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId", productId));

        Product product = modelMapper.map(productDTO, Product.class);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        existingProduct.setSpecialPrice(product.getSpecialPrice());



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

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRespository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);

        product.setImage(fileName);

        Product updatedProduct = productRespository.save(product);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

}
