package com.foodie.usermanagement;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

import com.foodie.models.Users;

@RestController
public class UserController {
    
    private Users users;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public String getUsers() {
        
        logger.info("UserController.getUsers() called with users: " + users);

        return "users";
    }

}
