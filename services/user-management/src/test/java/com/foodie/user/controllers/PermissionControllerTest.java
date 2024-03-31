package com.foodie.user.controllers;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.model.Permission;
import com.foodie.user.service.PermissionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({ TestSetupPostgres.class })
class PermissionControllerTest {

    private PermissionService permissionService;
    private PermissionController permissionController;

    @BeforeEach
    void setUp() {
        permissionService = mock(PermissionService.class);
        permissionController = new PermissionController(permissionService);
    }

    @Test
    void createPermission_ValidPermission_ReturnsCreatedResponse() {
        Permission permission = new Permission();
        permission.setId(UUID.randomUUID());
        when(permissionService.save(any(Permission.class))).thenReturn(permission);

        ResponseEntity<FoodieBaseResponse> response = permissionController.createPermission(permission);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(permission.getId(), ((Permission) response.getBody().data()).getId());
    }

    @Test
    void createPermission_NullPermission_ReturnsBadRequestResponse() {
        ResponseEntity<FoodieBaseResponse> response = permissionController.createPermission(null);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Permission cannot be null", response.getBody().data());
    }

    @Test
    void getPermissionById_ExistingId_ReturnsPermission() {
        Permission permission = new Permission();
        UUID permissionId = UUID.randomUUID();
        when(permissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.of(permission));

        ResponseEntity<FoodieBaseResponse> response = permissionController.getPermissionById(permissionId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(permission, response.getBody().data());
    }

    @Test
    void getPermissionById_NonExistingId_ReturnsNotFoundResponse() {
        UUID permissionId = UUID.randomUUID();
        when(permissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> response = permissionController.getPermissionById(permissionId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Permission not found", response.getBody().data());
    }

    @Test
    void getAllPermissions_ReturnsAllPermissions() {
        Page<Permission> permissions = mock(Page.class);
        when(permissionService.findAllByDeletedAtIsNull(0, 10, "id")).thenReturn(permissions);

        ResponseEntity<FoodieBaseResponse> response = permissionController.getAllPermissions(0, 10, "id");

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(permissions.getContent(), response.getBody().data());
    }
    
    @Test
    void updatePermission_ValidPermission_ReturnsUpdatedResponse() {
        UUID permissionId = UUID.randomUUID();
        Permission permission = new Permission();
        when(permissionService.updateById(any(Permission.class), eq(permissionId))).thenReturn(permission);

        ResponseEntity<FoodieBaseResponse> response = permissionController.updatePermission(permissionId, permission);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(permission, response.getBody().data());
    }

    @Test
    void deletePermission_NonExistingId_ReturnsBadRequestResponse() {
        UUID permissionId = UUID.randomUUID();
        when(permissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> response = permissionController.deletePermission(permissionId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Permission not found", response.getBody().data());
    }

    @Test
    void deletePermission_ExistingId_ReturnsAcceptedResponse() {
        UUID permissionId = UUID.randomUUID();
        when(permissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.of(new Permission()));

        ResponseEntity<FoodieBaseResponse> response = permissionController.deletePermission(permissionId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("", response.getBody().data());
    }

}
