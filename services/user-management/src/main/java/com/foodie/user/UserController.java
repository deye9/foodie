package com.foodie.user;

import org.springframework.web.bind.annotation.RestController;

import com.foodie.jwt.UserService;
import com.foodie.models.contracts.ChangePasswordRequest;
import com.foodie.models.users.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private User users;    
    private final UserService service;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping()
    public ResponseEntity<String> getUsers() {
        
        logger.info("UserController.getUsers() called with users: " + users);
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, Principal connectedUser) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
