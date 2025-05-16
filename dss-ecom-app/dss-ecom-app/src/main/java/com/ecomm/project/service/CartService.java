package com.ecomm.project.service;

import com.ecomm.project.payload.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    public CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO geCart(String emailId, Long cartId);
}
