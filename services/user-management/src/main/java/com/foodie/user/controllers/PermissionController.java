package com.foodie.user.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.model.Permission;
import com.foodie.user.service.PermissionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/security/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> createPermission(@Valid @RequestBody Permission permission) {
        if (permission == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FoodieBaseResponse("Permission cannot be null"));
        }

        log.info("Creating permission: {}", permission);

        Permission createdPermission = permissionService.save(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(createdPermission));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getPermissionById(@Valid @PathVariable("id") UUID id) {

        log.info("Fetching permission with ID: {}", id);

        Optional<Permission> permission = permissionService.findByIdAndDeletedAtIsNull(id);

        return permission.map(
                value -> ResponseEntity.ok().body(new FoodieBaseResponse(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new FoodieBaseResponse("Permission not found")));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getAllPermissions(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        log.info("Fetching all permissions with page={}, size={}, sort={}", page, size,
                sort);

        Page<Permission> permissions = permissionService.findAllByDeletedAtIsNull(page, size, sort);
        return ResponseEntity.ok().body(new FoodieBaseResponse(permissions.getContent()));
    }

    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> updatePermission(@Valid @PathVariable("id") UUID id,
            @Valid @RequestBody Permission permission) {

        log.info("Updating permission with ID: {}", id);

        Permission updatedPermission = permissionService.updateById(permission, id);
        return ResponseEntity.ok().body(new FoodieBaseResponse(updatedPermission));
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deletePermission(@Valid @PathVariable("id") UUID id) {

        log.info("Deleting permission with ID: {}", id);

        // Check if the permission exists
        Optional<Permission> permission = permissionService.findByIdAndDeletedAtIsNull(id);

        if (permission.isEmpty()) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Permission not found"));
        }

        permissionService.deleteById(id);
        return ResponseEntity.accepted().body(new FoodieBaseResponse(""));
    }

}