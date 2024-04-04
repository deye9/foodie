package com.foodie.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.contracts.ChangePasswordRequest;
import com.foodie.user.service.UserService;
import com.foodie.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping(path = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getUser(@Valid Principal connectedUser,
         @Valid @PathVariable("id") Optional<UUID> id) {

        log.info("Retrieving User");

        User retrievedUser = id.isPresent() ? service.validateAndGetUser(id.get())
                : service.validateAndGetUser(connectedUser);
        return ResponseEntity.ok().body(new FoodieBaseResponse(retrievedUser));
    }

    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> updateUser(@Valid @PathVariable("id") UUID id,
            @Valid @RequestBody User user, @Valid Principal connectedUser) {

        log.info("Updating User {}", connectedUser.getName());
        
        User connectedUserEntity = service.validateAndGetUser(connectedUser);
        if (!connectedUserEntity.getId().equals(id) || !connectedUserEntity.getEmail().equals(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FoodieBaseResponse("Invalid User ID or email"));
        }

        connectedUserEntity.setFirstname(user.getFirstname());
        connectedUserEntity.setLastname(user.getLastname());
        connectedUserEntity.setUpdatedAt(LocalDateTime.now());
        User updatedEntity = service.save(connectedUserEntity);
        return ResponseEntity.ok().body(new FoodieBaseResponse(updatedEntity));
    }

    @DeleteMapping(path = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deleteUser(@Valid Principal connectedUser,
    @Valid @PathVariable("id") Optional<UUID> id) {

        log.info("Deleting User");

        User retrievedUser = id.isPresent() ? service.validateAndGetUser(id.get())
                : service.validateAndGetUser(connectedUser);

                service.deleteById(retrievedUser.getId());
        return ResponseEntity.accepted().body(new FoodieBaseResponse(""));
    }

    @GetMapping(path = {"/deactivate", "/deactivate/{id}"}, produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deactivateUser(@Valid Principal connectedUser,
            @Valid @PathVariable("id") Optional<UUID> id) {

        log.info("Deactivating User");

        if (id.isPresent()) {
            service.deactivateUser(id.get());
        } else {
            service.deactivateUser(connectedUser);
        }

        return ResponseEntity.ok().body(new FoodieBaseResponse("User deactivated successfully"));
    }

    @PutMapping(path = "/password", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request,
            Principal connectedUser) {

        log.info("UserController.changePassword() has been called");

        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().body(new FoodieBaseResponse("Password changed successfully"));
    }
}
