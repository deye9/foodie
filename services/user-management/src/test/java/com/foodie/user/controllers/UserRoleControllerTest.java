package com.foodie.user.controllers;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.kafka.common.quota.ClientQuotaAlteration.Op;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.user.contracts.UserRoleRequest;
import com.foodie.user.model.Permission;
import com.foodie.user.model.Role;
import com.foodie.user.model.User;
import com.foodie.user.model.UserRole;
import com.foodie.FoodieBaseResponse;
import com.foodie.user.service.PermissionService;
import com.foodie.user.service.RolePermissionService;
import com.foodie.user.service.RoleService;
import com.foodie.user.service.UserRoleService;
import com.foodie.user.service.UserService;

@ExtendWith({ TestSetupPostgres.class, MockitoExtension.class })
public class UserRoleControllerTest {

    User user;
    Role role;
    UUID userId;
    UUID roleId;
    UUID randomID;
    String[] roleIds;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    UserRoleService userRoleService;

    @InjectMocks
    UserRoleController controller;

    @BeforeEach
    void setup() {
        randomID = UUID.randomUUID();

        user = new User();
        userId = UUID.randomUUID();
        user.setId(userId);

        role = new Role();
        roleId = UUID.randomUUID();
        role.setId(roleId);

        roleIds = new String[] { UUID.randomUUID().toString(), UUID.randomUUID().toString() };
    }

    @Test
    void createUserRole_WhenRoleIdsIsNull_ReturnsBadRequestResponse() {

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createUserRole(null, null);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Roles cannot be null", responseEntity.getBody().data());
    }

    @Test
    void createUserRole_EmptyRoles_ReturnsBadRequestResponse() {

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createUserRole(UUID.randomUUID(), new String[0]);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Roles cannot be null", responseEntity.getBody().data());
    }

    @Test
    void createUserRole_UserNotFound_ReturnsBadRequestResponse() {

        when(userService.validateAndGetUser(userId)).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createUserRole(userId,
                new String[] { "role1", "role2" });

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User not found.", responseEntity.getBody().data());
        verify(userService, times(1)).validateAndGetUser(userId);
    }

    @Test
    void createUserRole_RoleNotFound_ReturnsBadRequestResponse() {

        when(userService.validateAndGetUser(any(UUID.class))).thenReturn(user);
        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createUserRole(userId,
                new String[] { userId.toString() });

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(userService, times(1)).validateAndGetUser(any(UUID.class));
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
    }

    @Test
    void createUserRole_ValidRequest_ReturnsSuccessResponse() {

        Role role1 = new Role();
        Role role2 = new Role();
        role1.setId(UUID.fromString(roleIds[0]));
        role2.setId(UUID.fromString(roleIds[1]));

        when(userService.validateAndGetUser(userId)).thenReturn(user);
        when(roleService.validateAndGetRole(role1.getId())).thenReturn(role1);
        when(roleService.validateAndGetRole(role2.getId())).thenReturn(role2);
        when(userRoleService.saveAll(any())).thenReturn(new ArrayList<>());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.createUserRole(userId, roleIds);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(userService, times(1)).validateAndGetUser(userId);
        verify(roleService, times(2)).validateAndGetRole(any(UUID.class));
        verify(userRoleService, times(1)).saveAll(any());
    }

    @Test
    void getUserRoleByID_ExistingId_ReturnsSuccessResponse() {

        UserRole userRole = new UserRole();
        when(userRoleService.findByIdAndDeletedAtIsNull(any(UUID.class))).thenReturn(Optional.of(userRole));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getUserRoleByID(randomID);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userRoleService, times(1)).findByIdAndDeletedAtIsNull(any(UUID.class));
    }

    @Test
    void getUserRoleByID_NonExisitingId_ReturnsNotFoundResponse() {

        when(userRoleService.findByIdAndDeletedAtIsNull(randomID)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getUserRoleByID(randomID);

        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(userRoleService, times(1)).findByIdAndDeletedAtIsNull(randomID);
    }

    @Test
    void getAllPermissionsForRole_ReturnsAllRolePermissions() {

        int page = 0, size = 10;
        List<UserRole> userRoleList = new ArrayList<>();
        userRoleList.add(new UserRole());

        Page<UserRole> userRolePage = new PageImpl<>(userRoleList);
        when(userRoleService.findAllByUserIdAndDeletedAtIsNull(any(UUID.class), any(PageRequest.class)))
                .thenReturn(userRolePage);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getAllPermissionsForRole(randomID, page, size,
                "id");

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userRoleList, responseEntity.getBody().data());
        verify(userRoleService, times(1)).findAllByUserIdAndDeletedAtIsNull(any(UUID.class), any(PageRequest.class));
    }

    @Test
    void getAllPermissionsForRole_EmptyList_ReturnsEmptyList() {

        int page = 0, size = 10;
        List<UserRole> userRoleList = new ArrayList<>();
        Page<UserRole> userRolePage = new PageImpl<>(userRoleList);

        when(userRoleService.findAllByUserIdAndDeletedAtIsNull(any(UUID.class), any(PageRequest.class)))
                .thenReturn(userRolePage);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.getAllPermissionsForRole(userId, page, size,
                "id");

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userRoleList, responseEntity.getBody().data());
        verify(userRoleService, times(1)).findAllByUserIdAndDeletedAtIsNull(any(UUID.class), any(PageRequest.class));
    }

    @Test
    void patchRolePermission_UserNotFound_ReturnsBadRequestResponse() {

        when(userService.validateAndGetUser(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(userId, randomID, role);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User not found.", responseEntity.getBody().data());
        verify(userService, times(1)).validateAndGetUser(any(UUID.class));
    }

    @Test
    void patchRolePermission_RoleNotFound_ReturnsBadRequestResponse() {

        when(userService.validateAndGetUser(any(UUID.class))).thenReturn(user);
        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(userId, randomID, role);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(userService, times(1)).validateAndGetUser(any(UUID.class));
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
    }

    @Test
    void patchRolePermission_ValidRequest_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(userId)).thenReturn(user);
        when(roleService.validateAndGetRole(role.getId())).thenReturn(role);
        when(userRoleService.updateById(any(UserRole.class), any(UUID.class))).thenReturn(new UserRole());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.patchRolePermission(userId, randomID, role);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(userService, times(1)).validateAndGetUser(userId);
        verify(roleService, times(1)).validateAndGetRole(role.getId());
        verify(userRoleService, times(1)).updateById(any(UserRole.class), any(UUID.class));
    }

    @Test
    void updatePermission_UserNotFound_ReturnsBadRequestResponse() {

        when(userService.validateAndGetUser(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(userId, new UserRoleRequest[0]);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(userService, times(1)).validateAndGetUser(any(UUID.class));
    }

    @Test
    void updatePermission_RoleNotFound_ReturnsBadRequestResponse() {

        when(userService.validateAndGetUser(any(UUID.class))).thenReturn(user);
        when(roleService.validateAndGetRole(any(UUID.class))).thenReturn(null);

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(userId,
                new UserRoleRequest[] { new UserRoleRequest(null, role) });

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not found.", responseEntity.getBody().data());
        verify(userService, times(1)).validateAndGetUser(any(UUID.class));
        verify(roleService, times(1)).validateAndGetRole(any(UUID.class));
    }

    @Test
    void updatePermission_ValidRequest_ReturnsSuccessResponse() {

        when(userService.validateAndGetUser(userId)).thenReturn(user);
        when(roleService.validateAndGetRole(role.getId())).thenReturn(role);
        when(userRoleService.updateAll(any())).thenReturn(new ArrayList<>());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.updatePermission(userId,
                new UserRoleRequest[] { new UserRoleRequest(randomID.toString(), role) });

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(userService, times(1)).validateAndGetUser(userId);
        verify(roleService, times(1)).validateAndGetRole(role.getId());
        verify(userRoleService, times(1)).updateAll(any());
    }

    @Test
    void deleteUserRoleByID_NonExisitingId_ReturnsNotFoundResponse() {

        when(userRoleService.findByIdAndDeletedAtIsNull(randomID)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deleteUserRoleByID(randomID);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userRoleService, times(1)).findByIdAndDeletedAtIsNull(randomID);
    }

    @Test
    void deleteUserRoleByID_ExistingId_ReturnsSuccessResponse() {

        when(userRoleService.findByIdAndDeletedAtIsNull(randomID)).thenReturn(Optional.of(new UserRole()));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deleteUserRoleByID(randomID);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(userRoleService, times(1)).findByIdAndDeletedAtIsNull(randomID);
    }

    @Test
    void deleteRolesByUserID_NonExisitingId_ReturnsNotFoundResponse() {

        when(userRoleService.findAllByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deleteRolesByUserId(userId);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userRoleService, times(1)).findAllByUserIdAndDeletedAtIsNull(userId);
    }

    @Test
    void deleteRolesByUserID_ExistingId_ReturnsSuccessResponse() {

        when(userRoleService.findAllByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(new ArrayList<>()));

        ResponseEntity<FoodieBaseResponse> responseEntity = controller.deleteRolesByUserId(userId);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(userRoleService, times(1)).findAllByUserIdAndDeletedAtIsNull(userId);
    }

}
