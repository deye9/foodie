package com.foodie.user.controllers;

import com.github.javafaker.Faker;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.contracts.AuthenticationRequest;
import com.foodie.user.contracts.AuthenticationResponse;
import com.foodie.user.contracts.RegisterRequest;
import com.foodie.user.model.User;
import com.foodie.user.service.AuthenticationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({ TestSetupPostgres.class })
class AuthenticationControllerTest {

    private User user;
    private RegisterRequest request;
    private AuthenticationService authenticationService;
    private AuthenticationController authenticationController;
 
    @BeforeEach
    void setUp() {
        authenticationService = mock(AuthenticationService.class);
        authenticationController = new AuthenticationController(authenticationService);

        Faker faker = new Faker();
        request = new RegisterRequest(faker.name().firstName(), faker.name().lastName(),
                faker.internet().emailAddress(), faker.internet().password());

        user = new User(request.firstname(), request.lastname(), request.email(), request.password(), null);
    }

    @Test
    void contextLoads() {
        assertNotNull(authenticationController);
    }

    @Test
    void registerUser_Success() {        

        when(authenticationService.register(request)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = authenticationController.registerUser(request);

        verify(authenticationService).register(request);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
       assertEquals(user, Objects.requireNonNull(responseEntity.getBody()).data());
    }

    @Test
    void authenticateUser_Success() {

        AuthenticationRequest authRequest = new AuthenticationRequest(request.email(), request.password());
        AuthenticationResponse authResponse = new AuthenticationResponse(request.password(), request.password());

        when(authenticationService.authenticate(authRequest)).thenReturn(authResponse);

        ResponseEntity<FoodieBaseResponse> responseEntity = authenticationController.authenticateUser(authRequest);

        verify(authenticationService).authenticate(authRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
       assertEquals(authResponse, Objects.requireNonNull(responseEntity.getBody()).data());
    }

    @Test
    void refreshToken_Success() throws IOException, java.io.IOException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authenticationController.refreshToken(request, response);

        verify(authenticationService, times(1)).refreshToken(request, response);
    }
}
