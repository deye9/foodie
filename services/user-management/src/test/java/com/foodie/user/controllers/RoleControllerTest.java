package com.foodie.user.controllers;

import com.foodie.FoodieBaseResponse;
import com.foodie.user.model.Role;
import com.foodie.user.model.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRole_Success() {
        Role role = new Role();

        when(roleService.save(role)).thenReturn(role); // mock the role service save method

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.createRole(role);

        verify(roleService).save(role);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(role, Objects.requireNonNull(responseEntity.getBody()).getdata());
    }

    @Test
    void testGetRoleById() {
        UUID id = UUID.randomUUID();
        Role role = new Role();
        when(roleService.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(role));

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.getRoleById(id);

        verify(roleService).findByIdAndDeletedAtIsNull(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(role, Objects.requireNonNull(responseEntity.getBody()).getdata());
    }

    @Test
    void testGetRoleByID_NotFound() {
        UUID id = UUID.randomUUID();
        when(roleService.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.getRoleById(id);

        verify(roleService).findByIdAndDeletedAtIsNull(id);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Role not found", Objects.requireNonNull(responseEntity.getBody()).getdata());
    }

    @Test
    void testGetAllRoles_Success() {
        Page<Role> roles = mock(Page.class); // mock the Page object
        when(roleService.findAllByDeletedAtIsNull(0, 10, "id")).thenReturn(roles); // mock the role service method

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.getAllRoles(0, 10, "id");

        verify(roleService).findAllByDeletedAtIsNull(0, 10, "id");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(roles.getContent(), Objects.requireNonNull(responseEntity.getBody()).getdata());
    }

    @Test
    void testUpdateRole_Success() {
        UUID id = UUID.randomUUID();
        Role role = new Role();
        when(roleService.updateById(role, id)).thenReturn(role);

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.updateRole(id, role);

        verify(roleService).updateById(role, id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(role, Objects.requireNonNull(responseEntity.getBody()).getdata());
    }

    @Test
    void testDeleteRole_Success() {
        UUID id = UUID.randomUUID();

        ResponseEntity<FoodieBaseResponse> responseEntity = roleController.deleteRole(id);

        verify(roleService).deleteById(id);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("", Objects.requireNonNull(responseEntity.getBody()).getdata());
    }

}
