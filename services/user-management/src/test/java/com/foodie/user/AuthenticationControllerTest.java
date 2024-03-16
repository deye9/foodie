package com.foodie.user;

import com.github.javafaker.Faker;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.foodie.jwt.AuthenticationService;
import com.foodie.models.contracts.AuthenticationRequest;
import com.foodie.models.contracts.AuthenticationResponse;
import com.foodie.models.contracts.RegisterRequest;
import com.foodie.models.users.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({ TestDataSetup.class })
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    private User user;
    private RegisterRequest request;
    
    @BeforeEach
    void setUp() {
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

        ResponseEntity<?> responseEntity = authenticationController.registerUser(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", user), responseEntity.getBody());
        verify(authenticationService).register(request);
    }

    @Test
    void registedUser_Exception() {

        when(authenticationService.register(request)).thenThrow(RuntimeException.class);
        
        ResponseEntity<?> responseEntity = authenticationController.registerUser(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        verify(authenticationService).register(request);
    }

    @Test
    void authenticateUser_Success() {

        AuthenticationRequest authRequest = new AuthenticationRequest(request.email(), request.password());
        AuthenticationResponse authResponse = new AuthenticationResponse(request.password(), request.password());

        when(authenticationService.authenticate(authRequest)).thenReturn(authResponse);

        ResponseEntity<?> responseEntity = authenticationController.authenticateUser(authRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", authResponse), responseEntity.getBody());
        verify(authenticationService).authenticate(authRequest);
    }

    @Test
    void authenticateUser_Exception() {

        AuthenticationRequest authRequest = new AuthenticationRequest(request.email(), request.password());

        when(authenticationService.authenticate(authRequest)).thenThrow(RuntimeException.class);
        
        ResponseEntity<?> responseEntity = authenticationController.authenticateUser(authRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        verify(authenticationService).authenticate(authRequest);
    }

    @Test
    void refreshToken_Success() throws IOException, java.io.IOException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authenticationController.refreshToken(request, response);

        verify(authenticationService, times(1)).refreshToken(request, response);
    }
}
