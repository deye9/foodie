package com.foodie.menumanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multimodule.service.MyService;

@RestController()
public class MenuController {

    private MyService myService;

    private Logger logger = LoggerFactory.getLogger(MenuController.class);

    public MenuController(MyService myService) {
		this.myService = myService;
	}

    @GetMapping("/menu")
    public String getMenus() {
        logger.info("MenuController.getMenus() called");
        return myService.message();
    }
}
