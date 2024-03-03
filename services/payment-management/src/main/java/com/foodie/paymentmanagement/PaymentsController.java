package com.foodie.paymentmanagement;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class PaymentsController {
    private Logger logger = LoggerFactory.getLogger(PaymentsController.class);

    @GetMapping("/payments")
    public String getPayments() {
        logger.info("PaymentsController.getPayments() called");

        return "payments";
    }    
}
