package com.foodie.user.controllers;

import com.foodie.FoodieBaseResponse;
import org.springframework.web.bind.annotation.RestController;

import com.foodie.user.jwt.AuthenticationService;
import com.foodie.user.model.User;
import com.foodie.user.model.contracts.AuthenticationRequest;
import com.foodie.user.model.contracts.AuthenticationResponse;
import com.foodie.user.model.contracts.RegisterRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(path = "/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> registerUser(@Valid @RequestBody RegisterRequest request) {

        log.info("AuthenticationController.registerUser() called with request: {} " + request);

        User registedUser = service.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(registedUser));
    }

    @PostMapping(path = "/refresh-token", produces = "application/json", consumes = "application/json")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("AuthenticationController.refreshToken() called with request: " + request);

        service.refreshToken(request, response);
    }

    @PostMapping(path = "/authenticate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest request) {

        log.info("AuthenticationController.authenticateUser() called with request: " + request);

        AuthenticationResponse response = service.authenticate(request);

        return ResponseEntity.ok().body(new FoodieBaseResponse(response));
    }
}
