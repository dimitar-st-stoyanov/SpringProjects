package com.ecomm.project.service;

import com.ecomm.project.exceptions.APIException;
import com.ecomm.project.exceptions.ResourceNotFoundException;
import com.ecomm.project.model.Cart;
import com.ecomm.project.model.CartItem;
import com.ecomm.project.model.Product;
import com.ecomm.project.payload.CartDTO;
import com.ecomm.project.payload.ProductDTO;
import com.ecomm.project.repositories.CartItemRepository;
import com.ecomm.project.repositories.CartRepository;
import com.ecomm.project.repositories.ProductRespository;
import com.ecomm.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements  CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRespository productRespository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing or create new cart
        Cart cart = createCart();
        //Retrieve product details
        Product product = productRespository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        // Perform Validations
        if (cartItem != null){
            throw new APIException("Product" + product.getProductName() + "already exist in the cart!");
            }

        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName() + "is not available");
        }

        if(product.getQuantity() < quantity){
            throw new APIException("Please, make an order of the " + product.getProductName() +
                    " less than or equal to the quantity " + product.getQuantity());
        }

        //Create cart item
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        //Save cart item
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item ->{
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());
        //return updated cart
        return cartDTO;
    }

    private Cart createCart() {

        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
