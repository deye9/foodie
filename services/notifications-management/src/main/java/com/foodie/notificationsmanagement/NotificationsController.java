package com.foodie.notificationsmanagement;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class NotificationsController {
    private Logger logger = LoggerFactory.getLogger(NotificationsController.class);

    @GetMapping("/notifications")
    public String getNotifications() {
        logger.info("NotificationsController.getNotifications() called");

        return "notifications";
    }
}
