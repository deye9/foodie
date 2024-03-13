package com.foodie.menumanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MenuController {

    private final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @GetMapping("/menu")
    public String getMenus() {
        logger.info("MenuController.getMenus() called");
        return "Menu";
    }
}
