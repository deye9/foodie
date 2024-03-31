package com.foodie.user.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.FoodieBaseResponse;
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

    Role role;
    UUID roleId;
    UUID randomID;
    String[] permissionIds;

    @Mock
    private RoleService roleService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private RolePermissionController controller;

    @BeforeEach
    void setup() {
        randomID = UUID.randomUUID();

        role = new Role();
        roleId = UUID.randomUUID();
        role.setId(roleId);
        permissionIds = new String[] { UUID.randomUUID().toString(), UUID.randomUUID().toString() };
    }

    @Test
    void createRolePermission_NullPermission_ReturnsBadRequestResponse() {

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(null, null);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission cannot be null", responseEntity.getBody().data());
    }

    @Test
    void createRolePermission_EmptyPermission_ReturnsBadRequestResponse() {

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(UUID.randomUUID(),
                new String[0]);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission cannot be null", responseEntity.getBody().data());
    }

    @Test
    void createRolePermission_RoleNotFound_ReturnsBadRequestResponse() {

        when(roleService.validateAndGetRole(roleId)).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(roleId,
                new String[] { "permission1", "permission2" });

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).validateAndGetRole(roleId);
    }

    @Test
    void createRolePermission_PermissionNotFound_ReturnsBadRequestResponse() {

        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(role);
        when(permissionService.validateAndGetPermission(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(roleId, permissionIds);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
        verify(permissionService, times(1)).validateAndGetPermission(any(UUID.class));
    }

    @Test
    void createRolePermission_ValidRequest_ReturnsSuccessResponse() {

        Permission permission1 = new Permission();
        Permission permission2 = new Permission();
        permission1.setId(UUID.fromString(permissionIds[0]));
        permission2.setId(UUID.fromString(permissionIds[1]));

        when(roleService.validateAndGetRole(roleId)).thenReturn(role);
        when(permissionService.validateAndGetPermission(permission1.getId())).thenReturn(permission1);
        when(permissionService.validateAndGetPermission(permission2.getId())).thenReturn(permission2);
        when(rolePermissionService.saveAll(any())).thenReturn(new ArrayList<>());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createRolePermission(roleId, permissionIds);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(roleService, times(1)).validateAndGetRole(roleId);
        verify(permissionService, times(2)).validateAndGetPermission(any(UUID.class));
        verify(rolePermissionService, times(1)).saveAll(any());
    }

    @Test
    void getRolePermissionById_ExistingId_ReturnsPermissions() {

        RolePermission rolePermission = new RolePermission();
        when(rolePermissionService.findByIdAndDeletedAtIsNull(any(UUID.class))).thenReturn(Optional.of(rolePermission));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getRolePermissionById(randomID);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rolePermission, responseEntity.getBody().data());
        verify(rolePermissionService, times(1)).findByIdAndDeletedAtIsNull(any(UUID.class));
    }

    @Test
    void getRolePermissionById_NonExistingId_ReturnsNotFoundResponse() {

        when(rolePermissionService.findByIdAndDeletedAtIsNull(randomID)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getRolePermissionById(randomID);

        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).findByIdAndDeletedAtIsNull(randomID);
    }

    @Test
    void getAllPermissionsForRole_ReturnsAllRolePermissions() {

        int page = 0, size = 10;
        List<RolePermission> permissionsList = new ArrayList<>();
        permissionsList.add(new RolePermission());

        Page<RolePermission> permissionsPage = new PageImpl<>(permissionsList);
        when(rolePermissionService.findAllByRoleIdAndDeletedAtIsNull(any(UUID.class), any(PageRequest.class)))
                .thenReturn(permissionsPage);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getAllPermissionsForRole(randomID, page, size,
                "id");

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(permissionsList, responseEntity.getBody().data());
        verify(rolePermissionService, times(1)).findAllByRoleIdAndDeletedAtIsNull(any(UUID.class),
                any(PageRequest.class));
    }

    @Test
    void getAllPermissionsForRole_EmptyList_ReturnsEmptyList() {

        int page = 0, size = 10;
        List<RolePermission> permissionsList = new ArrayList<>();
        Page<RolePermission> permissionsPage = new PageImpl<>(permissionsList);

        when(rolePermissionService.findAllByRoleIdAndDeletedAtIsNull(any(UUID.class), any(PageRequest.class)))
                .thenReturn(permissionsPage);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getAllPermissionsForRole(roleId, page, size,
                "id");

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(permissionsList, responseEntity.getBody().data());
        verify(rolePermissionService, times(1)).findAllByRoleIdAndDeletedAtIsNull(any(UUID.class),
                any(PageRequest.class));
    }

    @Test
    void patchRolePermission_RoleNotFound_ReturnsBadRequestResponse() {

        Permission permissionRequest = new Permission();

        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(roleId, randomID,
                permissionRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
    }

    @Test
    void patchRolePermission_PermissionNotFound_ReturnsBadRequestResponse() {
       
        Permission permissionRequest = new Permission();
        permissionRequest.setId(randomID);

        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(role);
        when(permissionService.validateAndGetPermission(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(roleId, randomID,
                permissionRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
        verify(permissionService, times(1)).validateAndGetPermission(any(UUID.class));
    }

    @Test
    void patchRolePermission_ExistingPermission_ReturnsUpdatedPermission() {

        Permission permission = new Permission();
        Permission permissionRequest = new Permission();
        permissionRequest.setId(randomID);
        RolePermission rolePermission = new RolePermission(role, permission);

        when(roleService.validateAndGetRole(roleId)).thenReturn(role);
        when(permissionService.validateAndGetPermission(randomID)).thenReturn(permission);
        when(rolePermissionService.updateById(any(RolePermission.class), eq(randomID))).thenReturn(rolePermission);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(roleId, randomID,
                permissionRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(rolePermission, responseEntity.getBody().data());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(roleService, times(1)).validateAndGetRole(roleId);
        verify(permissionService, times(1)).validateAndGetPermission(randomID);
        verify(rolePermissionService, times(1)).updateById(any(RolePermission.class), eq(randomID));
    }

    @Test
    void updateRolePermission_RoleNotFound_ReturnsBadRequestResponse() {
  
        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(roleId, new RolePermitRequest[0]);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
    }

    @Test
    void updateRolePermission_PermissionNotFound_ReturnsBadRequestResponse() {
        
        Permission permissionRequest = new Permission();
        RolePermitRequest[] permissions = new RolePermitRequest[1];
        permissionRequest.setId(randomID);
        permissions[0] = new RolePermitRequest(UUID.randomUUID().toString(), permissionRequest);

        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(role);
        when(permissionService.validateAndGetPermission(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(roleId, permissions);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Permission not found.", responseEntity.getBody().data());
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
        verify(permissionService, times(1)).validateAndGetPermission(any(UUID.class));
    }

    @Test
    void updateRolePermission_ValidPermission_ReturnsUpdatedResponse() {
        
        Permission permission1 = new Permission();
        Permission permission2 = new Permission();
        Permission permissionRequest = new Permission();
        Permission permissionRequest1 = new Permission();
        permissionRequest.setId(randomID);
        permissionRequest1.setId(randomID);

        RolePermitRequest[] permissions = {
                new RolePermitRequest(permissionRequest.getId().toString(), permissionRequest),
                new RolePermitRequest(permissionRequest1.getId().toString(), permissionRequest1)
        };

        when(roleService.validateAndGetRole(roleId)).thenReturn(role);
        when(permissionService.validateAndGetPermission(permissionRequest.getId())).thenReturn(permission1);
        when(permissionService.validateAndGetPermission(permissionRequest1.getId())).thenReturn(permission2);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(roleId, permissions);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(roleService, times(1)).validateAndGetRole(roleId);
        verify(rolePermissionService, times(1)).updateAll(anyList());
    }

    @Test
    void deletePermissionByID_InvalidId_ReturnsNotFoundResponse() {

        when(rolePermissionService.findByIdAndDeletedAtIsNull(randomID)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByID(randomID);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).findByIdAndDeletedAtIsNull(randomID);
    }

    @Test
    void deletePermissionByID_ValidId_ReturnsAcceptedResponse() {

        RolePermission rolePermission = new RolePermission();

        when(rolePermissionService.findByIdAndDeletedAtIsNull(randomID)).thenReturn(Optional.of(rolePermission));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByID(randomID);

        assertNotNull(responseEntity.getBody());
        assertEquals("", responseEntity.getBody().data());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).deleteById(randomID);
    }

    @Test
    void deletePermissionByRoleId_InvalidId_ReturnsNotFoundResponse() {

        when(rolePermissionService.findAllByRoleIdAndDeletedAtIsNull(roleId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByRoleId(roleId);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).findAllByRoleIdAndDeletedAtIsNull(roleId);
    }

    @Test
    void deletePermissionByRoleId_ValidId_ReturnsAcceptedResponse() {

        RolePermission rolePermission = new RolePermission();

        when(rolePermissionService.findAllByRoleIdAndDeletedAtIsNull(roleId))
                .thenReturn(Optional.of(List.of(rolePermission)));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deletePermissionByRoleId(roleId);

        assertNotNull(responseEntity.getBody());
        assertEquals("", responseEntity.getBody().data());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(rolePermissionService, times(1)).findAllByRoleIdAndDeletedAtIsNull(roleId);
    }
}
