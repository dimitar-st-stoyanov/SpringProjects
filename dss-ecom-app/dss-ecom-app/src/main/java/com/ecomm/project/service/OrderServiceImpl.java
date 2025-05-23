package com.ecomm.project.service;

import com.ecomm.project.exceptions.APIException;
import com.ecomm.project.exceptions.ResourceNotFoundException;
import com.ecomm.project.model.*;
import com.ecomm.project.payload.OrderDTO;
import com.ecomm.project.payload.OrderItemDTO;
import com.ecomm.project.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements  OrderService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRespository productRespository;

    @Autowired
    CartServiceImpl cartService;

    @Autowired
    ModelMapper modelMapper;



    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        //Get user cart
        Cart cart = cartRepository.findCartByEmail(emailId);
        if(cart==null){
            throw new ResourceNotFoundException();
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException ("Address", "addressId", addressId));
        //Create a new order with payment info

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);


        //Get items from the cart

        List<CartItem> cartItems = cart.getCartItems();

        if(cartItems.isEmpty()){
            throw new APIException("Cart is empty!");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cartItems ){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        //Update product stock
        cart.getCartItems().forEach(item ->{
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);
            productRespository.save(product);

            //Clear the cart
            cartService.deleteProductFromCart(cart.getCartId(),
                                              item.getProduct().getProductId());
            });


        //Send back the order summary

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item ->orderDTO.getOrderItems()
                            .add(modelMapper.map(item, OrderItemDTO.class)));
        orderDTO.setAddressId(addressId);


        return orderDTO;

    }
}
