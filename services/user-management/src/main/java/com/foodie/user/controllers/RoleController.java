package com.foodie.user.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodie.enums.ResponseKeys;
import com.foodie.user.model.Role;
import com.foodie.user.model.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/roles")
public class RoleController {

    private final RoleService roleService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, ?>> createRole(@Valid @RequestBody Role role) {

        log.info("Creating role: {}", role);

        Role createdRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(ResponseKeys.DATA.toString(), createdRole));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Map<String, ?>> getRoleById(@Valid @PathVariable("id") UUID id) {

        log.info("Fetching role with ID: {}", id);

        Optional<Role> role = roleService.findByIdAndDeletedAtIsNull(id);

        return role.<ResponseEntity<Map<String, ?>>>map(
                value -> ResponseEntity.ok().body(Collections.singletonMap(ResponseKeys.DATA.toString(), value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap(ResponseKeys.DATA.toString(), "Role not found")));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Map<String, List<Role>>> getAllRoles(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        log.info("Fetching all roles with page={}, size={}, sort={}", page, size, sort);

        Page<Role> roles = roleService.findAllByDeletedAtIsNull(page, size, sort);
        List<Role> roleList = roles.getContent();
        return ResponseEntity.ok().body(Collections.singletonMap(ResponseKeys.DATA.toString(), roleList));
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Map<String, ?>> updateRole(@Valid @PathVariable("id") UUID id,
            @Valid @RequestBody Role role) {

        log.info("Updating role with ID: {}", id);

        Role updatedRole = roleService.updateById(role, id);
        return ResponseEntity.ok().body(Collections.singletonMap(ResponseKeys.DATA.toString(), updatedRole));
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Map<String, String>> deleteRole(@Valid @PathVariable("id") UUID id) {

        log.info("Deleting role with ID: {}", id);

        roleService.deleteById(id);
        return ResponseEntity.accepted().body(Collections.singletonMap(ResponseKeys.DATA.toString(), ""));
    }

}