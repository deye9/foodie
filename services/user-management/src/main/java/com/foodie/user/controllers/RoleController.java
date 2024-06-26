package com.foodie.user.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.model.Role;
import com.foodie.user.service.RoleService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/security/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> createRole(@Valid @RequestBody Role role) {
        if (role == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FoodieBaseResponse("Role cannot be null"));
        }

        log.info("Creating role: {}", role);

        Role createdRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(createdRole));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getRoleById(@Valid @PathVariable("id") UUID id) {

        log.info("Fetching role with ID: {}", id);

        Optional<Role> role = roleService.findByIdAndDeletedAtIsNull(id);

        return role.map(
                value -> ResponseEntity.ok().body(new FoodieBaseResponse(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new FoodieBaseResponse("Role not found")));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getAllRoles(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        log.info("Fetching all roles with page={}, size={}, sort={}", page, size,
                sort);

        Page<Role> roles = roleService.findAllByDeletedAtIsNull(page, size, sort);
        return ResponseEntity.ok().body(new FoodieBaseResponse(roles.getContent()));
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> updateRole(@Valid @PathVariable("id") UUID id,
            @Valid @RequestBody Role role) {

        log.info("Updating role with ID: {}", id);

        Role updatedRole = roleService.updateById(role, id);
        return ResponseEntity.ok().body(new FoodieBaseResponse(updatedRole));
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deleteRole(@Valid @PathVariable("id") UUID id) {

        log.info("Deleting role with ID: {}", id);

        // Check if the role exists
        Optional<Role> role = roleService.findByIdAndDeletedAtIsNull(id);

        if (role.isEmpty()) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
        }

        roleService.deleteById(id);
        return ResponseEntity.accepted().body(new FoodieBaseResponse(""));
    }

}