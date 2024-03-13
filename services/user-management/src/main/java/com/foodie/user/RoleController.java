package com.foodie.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodie.models.contracts.ResponseHandler;
import com.foodie.models.users.Role;
import com.foodie.service.RoleService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> createRole(@Valid @RequestBody Role role) {

        logger.info("RoleController.createRole() called with role: " + role);

        try {
            Role createdRole = roleService.createRole(role);

            if (createdRole == null) {
                return ResponseHandler.generateResponse(null, "Unable to process your request", HttpStatus.BAD_REQUEST);
            }

            return ResponseHandler.generateResponse(null, createdRole, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteRole(@Valid @PathVariable("id") UUID id) {

        logger.info("RoleController.deleteRole() called with id: " + id);

        try {
            Role role = roleService.deleteRole(id);

            if (role == null) {
                return ResponseHandler.generateResponse(null, "Role not found", HttpStatus.NOT_FOUND);
            }

            return ResponseHandler.generateResponse(null, "", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getRoleById(@Valid @PathVariable("id") UUID id) {

        logger.info("RoleController.getRoleById() called with id: " + id);

        try {
            Role role = roleService.getRoleById(id);

            if (role == null) {
                return ResponseHandler.generateResponse(null, "Role not found", HttpStatus.NOT_FOUND);
            }

            return ResponseHandler.generateResponse(null, role, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllRoles(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        logger.info("RoleController.getAllRoles() called with values: " + page + ", " + size + ", " + sort);

        List<Role> roles = roleService.getAllRoles(page, size, sort);

        return ResponseHandler.generateResponse(null, roles, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateRole(@Valid @PathVariable("id") UUID id, @Valid @RequestBody Role role) {

        logger.info("RoleController.updateRole() called with id: " + id + " and role: " + role);

        try {
            Role updatedRole = roleService.updateRole(id, role);

            if (updatedRole == null) {
                return ResponseHandler.generateResponse(null, "Unable to process your request", HttpStatus.BAD_REQUEST);
            }

            return ResponseHandler.generateResponse(null, updatedRole, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}