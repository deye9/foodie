package com.foodie.user;

import org.springframework.web.bind.annotation.RestController;

import com.foodie.jwt.AuthenticationService;
import com.foodie.models.contracts.AuthenticationRequest;
import com.foodie.models.contracts.AuthenticationResponse;
import com.foodie.models.contracts.RegisterRequest;
import com.foodie.models.contracts.ResponseHandler;
import com.foodie.models.users.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @PostMapping(path = "/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {

        logger.info("AuthenticationController.registerUser() called with request: " + request);

        try {
            User registedUser = service.register(request);

            return ResponseHandler.generateResponse(null, registedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/refresh-token", produces = "application/json", consumes = "application/json")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        logger.info("AuthenticationController.refreshToken() called with request: " + request);

        service.refreshToken(request, response);
    }

    @PostMapping(path = "/authenticate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest request) {

        logger.info("AuthenticationController.authenticateUser() called with request: " + request);

        try {
            AuthenticationResponse response = service.authenticate(request);

            return ResponseHandler.generateResponse(null, response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
