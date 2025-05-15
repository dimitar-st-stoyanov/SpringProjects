package com.ecomm.project.service;

import com.ecomm.project.payload.CartDTO;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    public CartDTO addProductToCart(Long productId, Integer quantity);
}
