package com.foodie.usermanagement;

import org.springframework.web.bind.annotation.RestController;

import com.foodie.library.services.UserService;
import com.foodie.models.contracts.ChangePasswordRequest;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService service;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
        logger.info("UserController.getUsers() called with request {} and user {}: ", request, connectedUser);

        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
