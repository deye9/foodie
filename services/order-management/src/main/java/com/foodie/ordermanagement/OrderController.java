package com.foodie.ordermanagement;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/orders")
    public String getOrders() {
        logger.info("OrderController.getOrders() called");

        return "orders";
    }
    
}
