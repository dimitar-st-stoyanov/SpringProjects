package com.ecomm.project.service;

import com.ecomm.project.payload.OrderDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
