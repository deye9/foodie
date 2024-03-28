package com.foodie.user.controllers;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.contracts.PermissionRequest;
import com.foodie.user.contracts.RolePermitRequest;
import com.foodie.user.model.Permission;
import com.foodie.user.model.Role;
import com.foodie.user.model.RolePermission;
import com.foodie.user.service.PermissionService;
import com.foodie.user.service.RolePermissionService;
import com.foodie.user.service.RoleService;
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
@RequestMapping("/api/v1/security/{role}/permissions")
public class RolePermissionController {

    private final RoleService roleService;
    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RoleService roleService, PermissionService permissionService,
            RolePermissionService rolePermissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
    }

    private Role validateAndGetRole(UUID roleId) {
        return roleService.findByIdAndDeletedAtIsNull(roleId).orElse(null);
    }

    private Permission validateAndGetPermission(UUID permitId) {
        return permissionService.findByIdAndDeletedAtIsNull(permitId).orElse(null);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> createRolePermission(@Valid @PathVariable("role") UUID roleId,
            @Valid @RequestBody String[] permissionIds) {

        if (permissionIds == null || permissionIds.length == 0) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Permission cannot be null"));
        }

        Role role = validateAndGetRole(roleId);
        if (role == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
        }

        List<RolePermission> rolePermissions = new ArrayList<>();

        for (String permitId : permissionIds) {
            Permission permission = validateAndGetPermission(UUID.fromString(permitId));
            if (permission == null) {
                return ResponseEntity.badRequest().body(new FoodieBaseResponse("Permission not found."));
            }
            rolePermissions.add(new RolePermission(role, permission));
        }

        log.info("Creating Role permissions");
        List<RolePermission> createdPermissions = rolePermissionService.saveAll(rolePermissions);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(createdPermissions));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getRolePermissionById(@Valid @PathVariable("id") UUID id) {

        log.info("Fetching Role permission with ID: {}", id);

        Optional<RolePermission> permission = rolePermissionService.findByIdAndDeletedAtIsNull(id);

        return permission.map(value -> ResponseEntity.ok().body(new FoodieBaseResponse(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> getAllPermissionsForRole(@Valid @PathVariable("role") UUID roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        log.info("Fetching all Role {} permissions with page={}, size={}, sort={}", roleId, page, size, sort);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<RolePermission> permissions = rolePermissionService.findAllByRoleIdAndDeletedAtIsNull(roleId, pageable);
        return ResponseEntity.ok().body(new FoodieBaseResponse(permissions.getContent()));
    }

    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> patchRolePermission(@Valid @PathVariable("role") UUID roleId,
            @Valid @PathVariable("id") UUID id,
            @Valid @RequestBody PermissionRequest permission) {

        log.info("Updating Role {}'s Permission(s)", roleId);

        Role role = validateAndGetRole(roleId);
        if (role == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
        }

        Permission permissionEntity = validateAndGetPermission(UUID.fromString(permission.id()));
        if (permissionEntity == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Permission not found."));
        }

        RolePermission updatedPermission = rolePermissionService.updateById(new RolePermission(role, permissionEntity),
                id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(updatedPermission));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodieBaseResponse> updatePermission(@Valid @PathVariable("role") UUID roleId,
            @Valid @RequestBody RolePermitRequest[] permissions) {

        log.info("Updating permissions for Role: {}", roleId);

        List<RolePermission> rolePermissions = new ArrayList<>();

        Role role = validateAndGetRole(roleId);
        if (role == null) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Role not found."));
        }

        for (RolePermitRequest permit : permissions) {
            Permission permissionEntity = validateAndGetPermission(UUID.fromString(permit.permission().id()));
            if (permissionEntity == null) {
                return ResponseEntity.badRequest().body(new FoodieBaseResponse("Permission not found."));
            }

            RolePermission rolePermission = new RolePermission(role, permissionEntity);
            rolePermission.setId(UUID.fromString(permit.id()));
            rolePermissions.add(rolePermission);
        }

        log.info("Updating Role permissions");
        List<RolePermission> updatedPermissions = rolePermissionService.updateAll(rolePermissions);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FoodieBaseResponse(updatedPermissions));
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deletePermissionByID(@Valid @PathVariable("id") UUID id) {

        log.info("Deleting Role permission with ID: {}", id);

        // Check if the permission exists
        Optional<RolePermission> permission = rolePermissionService.findByIdAndDeletedAtIsNull(id);

        if (permission.isEmpty()) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Permission not found."));
        }

        rolePermissionService.deleteById(id);
        return ResponseEntity.accepted().body(new FoodieBaseResponse(""));
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<FoodieBaseResponse> deletePermissionByRoleId(@Valid @PathVariable("role") UUID roleId) {

        log.info("Deleting permissions for Role: {}", roleId);

        // Check if the permission exists
        Optional<RolePermission> permission = rolePermissionService.findByIdAndDeletedAtIsNull(roleId);

        if (permission.isEmpty()) {
            return ResponseEntity.badRequest().body(new FoodieBaseResponse("Permission not found."));
        }

        rolePermissionService.deleteAllByRoleId(roleId);
        return ResponseEntity.accepted().body(new FoodieBaseResponse(""));
    }
}