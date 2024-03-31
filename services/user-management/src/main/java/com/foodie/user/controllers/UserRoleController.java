package com.foodie.user.controllers;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.service.UserService;
import com.foodie.user.contracts.UserRoleRequest;
import com.foodie.user.model.Role;
import com.foodie.user.model.User;
import com.foodie.user.model.UserRole;
import com.foodie.user.service.RoleService;
import com.foodie.user.service.UserRoleService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/security/{userId}/role")
public class UserRoleController {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRoleService userRoleService;

    public UserRoleController(RoleService roleService, UserService userService,
            UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> createUserRole(@Valid @PathVariable("userId") UUID userId,
            @Valid @RequestBody String[] roleIds) {

        if (roleIds == null || roleIds.length == 0) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Roles cannot be null"));
        }

        User user = userService.validateAndGetUser(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("User not found."));
        }

        List<UserRole> userRoles = new ArrayList<>();

        for (String roleId : roleIds) {
            Role role = roleService.validateAndGetRole(UUID.fromString(roleId));
            if (role == null) {
                return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
            }
            userRoles.add(new UserRole(user, role));
        }

        log.info("Attaching Roles to User");
        List<UserRole> createdEntity = userRoleService.saveAll(userRoles);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(createdEntity));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getUserRoleByID(@Valid @PathVariable("id") UUID id) {

        log.info("Fetching User role with ID: {}", id);

        Optional<UserRole> retrievedEntity = userRoleService.findByIdAndDeletedAtIsNull(id);

        return retrievedEntity.map(value -> ResponseEntity.ok().body(new FoodieBaseResponse(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getAllPermissionsForRole(@Valid @PathVariable("userId") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        log.info("Fetching all User {} roles with page={}, size={}, sort={}", userId,
                page, size, sort);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<UserRole> retrievedEntities = userRoleService.findAllByUserIdAndDeletedAtIsNull(userId, pageable);
        return ResponseEntity.ok().body(new FoodieBaseResponse(retrievedEntities.getContent()));
    }

    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> patchRolePermission(@Valid @PathVariable("userId") UUID userId,
            @Valid @PathVariable("id") UUID id,
            @Valid @RequestBody Role role) {

        log.info("Updating User {}'s Role(s)", userId);

        User user = userService.validateAndGetUser(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("User not found."));
        }

        Role roleEntity = roleService.validateAndGetRole(role.getId());
        if (roleEntity == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
        }

        UserRole updatedEntity = userRoleService.updateById(new UserRole(user,
                roleEntity), id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(updatedEntity));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> updatePermission(@Valid @PathVariable("userId") UUID userId,
            @Valid @RequestBody UserRoleRequest[] roles) {

        log.info("Updating Roles for User: {}", userId);

        List<UserRole> userRoles = new ArrayList<>();

        User user = userService.validateAndGetUser(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
        }

        for (UserRoleRequest role : roles) {
            Role roleEntity = roleService.validateAndGetRole(role.role().getId());
            if (roleEntity == null) {
                return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
            }

            UserRole newEntity = new UserRole(user, roleEntity);
            newEntity.setId(UUID.fromString(role.id()));
            userRoles.add(newEntity);
        }

        log.info("Updating User Roles");
        List<UserRole> updatedEntity = userRoleService.updateAll(userRoles);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(updatedEntity));
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deleteUserRoleByID(@Valid @PathVariable("id") UUID id) {

        log.info("Deleting Role permission with ID: {}", id);

        // Check if the user role exists
        Optional<UserRole> deletedEntity = userRoleService.findByIdAndDeletedAtIsNull(id);

        if (deletedEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("User role not found."));
        }

        userRoleService.deleteById(id);
        return ResponseEntity.accepted().body(new FoodieBaseResponse(""));
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deleteRolesByUserId(@Valid @PathVariable("userId") UUID userId) {

        log.info("Deleting roles for User: {}", userId);

        // Check if the user role exists
        Optional<List<UserRole>> deletedEntity = userRoleService.findAllByUserIdAndDeletedAtIsNull(userId);

        if (deletedEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("User role not found."));
        }

        deletedEntity.ifPresent(entity -> entity.forEach(userRoleService::delete));

        return ResponseEntity.accepted().body(new FoodieBaseResponse(""));
    }
}