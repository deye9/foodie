package com.foodie.restaurantsmanagement;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class RestaurantsController {
    private Logger logger = LoggerFactory.getLogger(RestaurantsController.class);

    @GetMapping("/restaurants")
    public String getRestaurants() {
        logger.info("RestaurantsController.getRestaurants() called");

        return "restaurants";
    }
}
