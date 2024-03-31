package com.foodie.user.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.contracts.PermissionRequest;
import com.foodie.user.contracts.RolePermitRequest;
import com.foodie.user.model.Permission;
import com.foodie.user.model.Role;
import com.foodie.user.model.RolePermission;
import com.foodie.user.service.PermissionService;
import com.foodie.user.service.RolePermissionService;
import com.foodie.user.service.RoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({ TestSetupPostgres.class, MockitoExtension.class })
class RolePermissionControllerTest {

    @Mock
    private RoleService roleService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private RolePermissionController controller;

    @Test
    void createRolePermission_NullPermission_ReturnsBadRequestResponse() {
        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(null, null);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission cannot be null", responseEntity.getBody().data());
    }

    @Test
    void createRolePermission_RoleNotFound_ReturnsBadRequestResponse() {
        UUID roleId = UUID.randomUUID();
        String[] permissionIds = new String[] { "permission1", "permission2" };

        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(roleId,
                permissionIds);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
    }

    @Test
    void createRolePermission_PermissionNotFound_ReturnsBadRequestResponse() {
        UUID roleId = UUID.randomUUID();
        String[] permissionIds = new String[] { UUID.randomUUID().toString(), UUID.randomUUID().toString() };

        Role role = new Role();
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(role));

        when(permissionService.findByIdAndDeletedAtIsNull(UUID.fromString(permissionIds[0])))
                .thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(roleId,
                permissionIds);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(UUID.fromString(permissionIds[0]));
    }

    @Test
    void testCreateRolePermission_ReturnsCreatedPermissions() {
        UUID roleId = UUID.randomUUID();
        String[] permissionIds = new String[] { UUID.randomUUID().toString(), UUID.randomUUID().toString() };

        Role role = new Role();
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(role));

        Permission permission1 = new Permission();
        when(permissionService.findByIdAndDeletedAtIsNull(UUID.fromString(permissionIds[0])))
                .thenReturn(Optional.of(permission1));

        Permission permission2 = new Permission();
        when(permissionService.findByIdAndDeletedAtIsNull(UUID.fromString(permissionIds[1])))
                .thenReturn(Optional.of(permission2));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(roleId,
                permissionIds);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).saveAll(anyList());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(UUID.fromString(permissionIds[0]));
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(UUID.fromString(permissionIds[1]));
    }

    @Test
    void getRolePermissionById_ExistingId_ReturnsPermissions() {
        RolePermission rolePermission = new RolePermission();
        UUID permissionId = UUID.randomUUID();
        when(rolePermissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.of(rolePermission));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getRolePermissionById(permissionId);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rolePermission, responseEntity.getBody().data());
        verify(rolePermissionService, times(1)).findByIdAndDeletedAtIsNull(permissionId);
    }

    @Test
    void getRolePermissionById_NonExistingId_ReturnsNotFoundResponse() {
        UUID permissionId = UUID.randomUUID();
        when(rolePermissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getRolePermissionById(permissionId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).findByIdAndDeletedAtIsNull(permissionId);
    }

    @Test
    void getAllPermissionsForRole_ReturnsAllRolePermissions() {
        int page = 0;
        int size = 10;
        String sort = "id";
        UUID roleId = UUID.randomUUID();
        List<RolePermission> permissionsList = new ArrayList<>();
        permissionsList.add(new RolePermission());

        Page<RolePermission> permissionsPage = new PageImpl<>(permissionsList);
        when(rolePermissionService.findAllByRoleIdAndDeletedAtIsNull(roleId, PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(permissionsPage);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getAllPermissionsForRole(roleId, page, size,
                sort);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(permissionsList, responseEntity.getBody().data());
        verify(rolePermissionService, times(1))
                .findAllByRoleIdAndDeletedAtIsNull(roleId, PageRequest.of(page, size, Sort.by(sort)));
    }

    @Test
    void getAllPermissionsForRole_EmptyList_ReturnsEmptyList() {
        int page = 0;
        int size = 10;
        String sort = "id";
        UUID roleId = UUID.randomUUID();
        List<RolePermission> permissionsList = new ArrayList<>();

        Page<RolePermission> permissionsPage = new PageImpl<>(permissionsList);
        when(rolePermissionService.findAllByRoleIdAndDeletedAtIsNull(roleId, PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(permissionsPage);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getAllPermissionsForRole(roleId, page, size,
                sort);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(permissionsList, responseEntity.getBody().data());
        verify(rolePermissionService, times(1))
                .findAllByRoleIdAndDeletedAtIsNull(roleId, PageRequest.of(page, size, Sort.by(sort)));
    }

    @Test
    void patchRolePermission_RoleNotFound_ReturnsBadRequestResponse() {
        UUID roleId = UUID.randomUUID();
        UUID permissionId = UUID.randomUUID();
        PermissionRequest permissionRequest = new PermissionRequest(permissionId.toString(), null, null, null, null,
                null, false, false, false, false);

        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(roleId, permissionId,
                permissionRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
    }

    @Test
    void patchRolePermission_PermissionNotFound_ReturnsBadRequestResponse() {
        UUID roleId = UUID.randomUUID();
        UUID permissionId = UUID.randomUUID();
        PermissionRequest permissionRequest = new PermissionRequest(permissionId.toString(), null, null, null, null,
                null, false, false, false, false);

        Role role = new Role();
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(role));

        when(permissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(roleId, permissionId,
                permissionRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(permissionId);
    }

    @Test
    void patchRolePermission_ExistingPermission_ReturnsUpdatedPermission() {
        UUID roleId = UUID.randomUUID();
        UUID permissionId = UUID.fromString("3769aa5e-1195-4e90-b738-e5455ac2d00c");
        PermissionRequest permissionRequest = new PermissionRequest(permissionId.toString(), null, null, null, null,
                null, false, false, false, false);

        Role role = new Role();
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(role));

        Permission permission = new Permission();
        when(permissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.of(permission));

        RolePermission rolePermission = new RolePermission(role, permission);
        when(rolePermissionService.updateById(any(RolePermission.class), eq(permissionId))).thenReturn(rolePermission);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(roleId, permissionId,
                permissionRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(rolePermission, responseEntity.getBody().data());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(permissionId);
        verify(rolePermissionService, times(1)).updateById(any(RolePermission.class), eq(permissionId));
    }

    @Test
    void updateRolePermission_RoleNotFound_ReturnsBadRequestResponse() {
        UUID roleId = UUID.randomUUID();
        UUID permissionId = UUID.fromString("3769aa5e-1195-4e90-b738-e5455ac2d00c");
        PermissionRequest permissionRequest = new PermissionRequest(permissionId.toString(), null, null, null, null,
                null, false, false, false, false);
        RolePermitRequest[] permissions = new RolePermitRequest[1];
        permissions[0] = new RolePermitRequest(UUID.randomUUID().toString(), permissionRequest);

        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(roleId, permissions);

        assertNotNull(responseEntity.getBody());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
    }

    @Test
    void updateRolePermission_PermissionNotFound_ReturnsBadRequestResponse() {
        UUID roleId = UUID.randomUUID();
        UUID permissionId = UUID.fromString("3769aa5e-1195-4e90-b738-e5455ac2d00c");

        PermissionRequest permissionRequest = new PermissionRequest(permissionId.toString(), null, null, null, null,
                null, false, false, false, false);
        RolePermitRequest[] permissions = new RolePermitRequest[1];
        permissions[0] = new RolePermitRequest(UUID.randomUUID().toString(), permissionRequest);

        Role role = new Role();
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(role));

        when(permissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(roleId, permissions);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(permissionId);
    }

    @Test
    void updateRolePermission_ValidPermission_ReturnsUpdatedResponse() {
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        when(roleService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(role));

        UUID id = UUID.fromString("3769aa5e-1195-4e90-b738-e5455ac2d00c");
        PermissionRequest permissionRequest = new PermissionRequest(id.toString(), null, null, null, null,
                null, false, false, false, false);
        Permission permission1 = new Permission();
        when(permissionService.findByIdAndDeletedAtIsNull(id))
                .thenReturn(Optional.of(permission1));

        UUID id1 = UUID.fromString("4769aa5e-1195-4e90-b738-e5455ac2d00c");
        PermissionRequest permissionRequest1 = new PermissionRequest(id1.toString(), null, null, null, null,
                null, false, false, false, false);
        Permission permission2 = new Permission();
        when(permissionService.findByIdAndDeletedAtIsNull(id1))
                .thenReturn(Optional.of(permission2));

        RolePermitRequest[] permissions = {
                new RolePermitRequest(id.toString(), permissionRequest),
                new RolePermitRequest(id1.toString(), permissionRequest1)
        };

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(roleId, permissions);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).updateAll(anyList());
        verify(roleService, times(1)).findByIdAndDeletedAtIsNull(roleId);
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(id);
        verify(permissionService, times(1)).findByIdAndDeletedAtIsNull(id1);
    }

    @Test
    void deletePermissionByID_InvalidId_ReturnsNotFoundResponse() {
        UUID permissionId = UUID.randomUUID();
        when(rolePermissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByID(permissionId);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).findByIdAndDeletedAtIsNull(permissionId);
    }

    @Test
    void deletePermissionByID_ValidId_ReturnsAcceptedResponse() {
        UUID permissionId = UUID.randomUUID();
        RolePermission rolePermission = new RolePermission();
        when(rolePermissionService.findByIdAndDeletedAtIsNull(permissionId)).thenReturn(Optional.of(rolePermission));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByID(permissionId);

        assertNotNull(responseEntity.getBody());
        assertEquals("", responseEntity.getBody().data());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).deleteById(permissionId);
    }

    @Test
    void deletePermissionByRoleId_InvalidId_ReturnsNotFoundResponse() {
        UUID roleId = UUID.randomUUID();
        when(rolePermissionService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByRoleId(roleId);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).findByIdAndDeletedAtIsNull(roleId);
    }

    @Test
    void deletePermissionByRoleId_ValidId_ReturnsAcceptedResponse() {
        UUID roleId = UUID.randomUUID();
        RolePermission rolePermission = new RolePermission();
        when(rolePermissionService.findByIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.of(rolePermission));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByRoleId(roleId);

        assertNotNull(responseEntity.getBody());
        assertEquals("", responseEntity.getBody().data());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).deleteAllByRoleId(roleId);
    }
}
