package com.foodie.ridersmanagement;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class RidersController {
    private Logger logger = LoggerFactory.getLogger(RidersController.class);

    @GetMapping("/riders")
    public String getRiders() {
        logger.info("RidersController.getRiders() called");

        return "riders";
    }
}
