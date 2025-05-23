package com.ecomm.project.controller;

import com.ecomm.project.payload.OrderDTO;
import com.ecomm.project.payload.OrderRequestDTO;
import com.ecomm.project.service.OrderService;
import com.ecomm.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@RequestBody OrderRequestDTO orderRequestDTO,
                                                  @PathVariable String paymentMethod){

        String emailId = authUtil.loggedInEmail();

      OrderDTO order =   orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
      return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);
    }
}
