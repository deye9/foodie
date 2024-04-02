package com.foodie.user.controllers;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.model.Role;
import com.foodie.user.service.RoleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    private RoleService roleService;
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        roleService = mock(RoleService.class);
        roleController = new RoleController(roleService);
    }

    @Test
    void createRole_ValidRole_ReturnsCreatedResponse() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        when(roleService.save(any(Role.class))).thenReturn(role);

        ResponseEntity<FoodieBaseResponse> response = roleController.createRole(role);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(role.getId(), ((Role) response.getBody().data()).getId());
    }

    @Test
    void createRole_NullRole_ReturnsBadRequestResponse() {
        ResponseEntity<FoodieBaseResponse> response = roleController.createRole(null);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Role cannot be null", response.getBody().data());
    }

    @Test
    void getRoleById_ExistingId_ReturnsRole() {
        Role role = new Role();
        UUID roleId = UUID.randomUUID();
        role.setId(roleId);
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(role));

        ResponseEntity<FoodieBaseResponse> response = roleController.getRoleById(roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(roleId, ((Role)response.getBody().data()).getId());
    }

    @Test
    void getRoleById_NonExistingId_ReturnsNotFoundResponse() {
        UUID roleId = UUID.randomUUID();
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> response = roleController.getRoleById(roleId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Role not found", response.getBody().data());
    }

    @Test
    void getAllRoles_ReturnsAllRoles() {
        Page<Role> roles = mock(Page.class);
        when(roleService.findAllByDeletedAtIsNull(0, 10, "id")).thenReturn(roles);

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.getAllRoles(0, 10, "id");

        verify(roleService).findAllByDeletedAtIsNull(0, 10, "id");
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(roles.getContent(), Objects.requireNonNull(responseEntity.getBody()).data());
    }

    @Test
    void updateRole_ValidRole_ReturnsUpdatedResponse() {
        UUID id = UUID.randomUUID();
        Role role = new Role();
        when(roleService.updateById(role, id)).thenReturn(role);

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.updateRole(id, role);

        verify(roleService).updateById(role, id);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(role, Objects.requireNonNull(responseEntity.getBody()).data());
    }
 
    @Test
    void deleteRole_NonExistingId_ReturnsBadRequestResponse() {
        UUID id = UUID.randomUUID();
        when(roleService.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.deleteRole(id);

        verify(roleService, never()).deleteById(id);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
    }

    @Test
    void deleteRole_ExistingId_ReturnsAcceptedResponse() {
        UUID id = UUID.randomUUID();
        when(roleService.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(new Role()));

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.deleteRole(id);

        verify(roleService).deleteById(id);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody().data());
    }
}
