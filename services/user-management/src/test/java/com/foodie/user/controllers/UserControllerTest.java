package com.foodie.user.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.model.User;
import com.foodie.user.service.UserService;

public class UserControllerTest {

    User user;
    UUID randomId;
    Principal principal;
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setup() {
        user = new User();
        randomId = UUID.randomUUID();
        user.setId(randomId);
        principal = mock(Principal.class);
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void getUser_ValidId_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(randomId)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.getUser(null, Optional.of(randomId));

        assertNotNull(responseEntity.getBody());
        assertEquals(user, responseEntity.getBody().data());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).validateAndGetUser(randomId);
        verify(userService, times(0)).validateAndGetUser(principal);
    }

    @Test
    void getUser_ValidPrincipal_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(principal)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.getUser(principal, Optional.empty());

        assertNotNull(responseEntity.getBody());
        assertEquals(user, responseEntity.getBody().data());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).validateAndGetUser(principal);
        verify(userService, times(0)).validateAndGetUser(randomId);
    }

    @Test
    void getUser_InvalidData_ReturnsIllegalArgumentException() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userController.getUser(null, Optional.empty()));

        String expectedMessage = "Data cannot be null";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void getUser_ValidId_WithPrincipal_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(randomId)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.getUser(principal, Optional.of(randomId));

        assertNotNull(responseEntity.getBody());
        assertEquals(user, responseEntity.getBody().data());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).validateAndGetUser(randomId);
        verify(userService, times(0)).validateAndGetUser(principal);
    }

    @Test
    void updateUser_InvalidID_ReturnsBadRequestResponse() {

        User updatedUser = new User();
        updatedUser.setId(randomId);
        updatedUser.setEmail("john.doe@gmail.com");
        updatedUser.setFirstname("John");
        updatedUser.setLastname("Doe");
        updatedUser.setUpdatedAt(LocalDateTime.now());

        user.setId(UUID.randomUUID());
        user.setEmail("john.doe@gmail.com");

        when(userService.validateAndGetUser(principal)).thenReturn(user);
        when(userService.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.updateUser(randomId, updatedUser, principal);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userService, times(0)).save(any(User.class));
        verify(userService, times(1)).validateAndGetUser(principal);
    }

    @Test
    void updateUser_InvalidEmail_ReturnsBadRequestResponse() {

        User updatedUser = new User();
        updatedUser.setId(randomId);
        updatedUser.setEmail("john.doe@gmail.com");
        updatedUser.setFirstname("John");
        updatedUser.setLastname("Doe");
        updatedUser.setUpdatedAt(LocalDateTime.now());

        user.setId(UUID.randomUUID());
        user.setEmail("john@gmail.com");

        when(userService.validateAndGetUser(principal)).thenReturn(user);
        when(userService.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.updateUser(randomId, updatedUser, principal);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userService, times(0)).save(any(User.class));
        verify(userService, times(1)).validateAndGetUser(principal);
    }

    @Test
    void updateUser_ValidData_ReturnsSuccessResponse() {

        User updatedUser = new User();
        updatedUser.setId(randomId);
        updatedUser.setLastname("Doe");
        updatedUser.setFirstname("John");
        updatedUser.setEmail("john.doe@gmail.com");
        updatedUser.setUpdatedAt(LocalDateTime.now());

        user.setId(randomId);
        user.setEmail("john.doe@gmail.com");

        when(userService.validateAndGetUser(principal)).thenReturn(user);
        when(userService.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.updateUser(randomId, updatedUser, principal);

        assertNotNull(responseEntity.getBody());
        assertNotNull(updatedUser.getUpdatedAt());
        assertEquals(updatedUser.getId(), user.getId());
        assertEquals(updatedUser.getEmail(), user.getEmail());
        assertEquals(updatedUser.getFirstname(), user.getFirstname());
        assertEquals(updatedUser.getLastname(), user.getLastname());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).save(any(User.class));
        verify(userService, times(1)).validateAndGetUser(principal);
    }

    @Test
    void deleteUser_ValidData_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(randomId)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.deleteUser(null, Optional.of(randomId));

        assertNotNull(responseEntity.getBody());
        assertEquals("", responseEntity.getBody().data());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(userService, times(1)).deleteById(randomId);
        verify(userService, times(1)).validateAndGetUser(randomId);
        verify(userService, times(0)).validateAndGetUser(principal);
    }

    @Test
    void deleteUser_ValidPrincipal_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(principal)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.deleteUser(principal, Optional.empty());

        assertNotNull(responseEntity.getBody());
        assertEquals("", responseEntity.getBody().data());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(userService, times(1)).validateAndGetUser(principal);
        verify(userService, times(0)).validateAndGetUser(randomId);
    }

    @Test
    void deleteUser_ValidID_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(randomId)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.deleteUser(null, Optional.of(randomId));

        assertNotNull(responseEntity.getBody());
        assertEquals("", responseEntity.getBody().data());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(userService, times(1)).deleteById(randomId);
        verify(userService, times(1)).validateAndGetUser(randomId);
        verify(userService, times(0)).validateAndGetUser(principal);
    }

    @Test
    void deactivateUser_ValidData_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(randomId)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.deactivateUser(principal,
                Optional.of(randomId));

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).deactivateUser(randomId);
        verify(userService, times(0)).deactivateUser(principal);

    }

    @Test
    void deactivateUser_ValidPrincipal_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(principal)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.deactivateUser(principal, Optional.empty());

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).deactivateUser(principal);
        verify(userService, times(0)).deactivateUser(randomId);
    }

    @Test
    void deactivateUser_ValidID_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(randomId)).thenReturn(user);

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.deactivateUser(null,
                Optional.of(randomId));

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).deactivateUser(randomId);
        verify(userService, times(0)).deactivateUser(principal);
    }

    @Test
    void changePassword_ValidRequest_ReturnsSuccessResponse() {

        ResponseEntity<FoodieBaseResponse> responseEntity = userController.changePassword(null, principal);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
