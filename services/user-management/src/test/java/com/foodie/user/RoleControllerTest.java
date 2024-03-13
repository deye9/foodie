package com.foodie.user;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.models.users.Role;
import com.foodie.service.RoleService;
import com.github.javafaker.Faker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({ TestDataSetup.class })
class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    private final Faker faker = new Faker();

    private Role createTestRole() {
        return new Role(UUID.randomUUID(), faker.name().username(), faker.book().toString(), null, null, null);
    }

    private UUID createTestUUID() {
        return UUID.randomUUID();
    }

    @Test
    void contextLoads() {
        assertNotNull(roleController);
    }

    // Create Role Test
    @Test
    void createRole_Success() {
        Role role = createTestRole();

        when(roleService.createRole(role)).thenReturn(role);

        ResponseEntity<?> responseEntity = roleController.createRole(role);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", role), responseEntity.getBody());
        verify(roleService).createRole(role);
    }

    @Test
    void createRole_BadRequest() {
        Role role = createTestRole();

        when(roleService.createRole(role)).thenReturn(null);

        ResponseEntity<?> responseEntity = roleController.createRole(role);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", "Unable to process your request"), responseEntity.getBody());
        verify(roleService).createRole(role);
    }

    @Test
    void createRole_Exception() {
        Role role = createTestRole();

        when(roleService.createRole(role)).thenThrow(
                new RuntimeException("{status: 400, message: 'An Error Occurred', timestamp: '2021-08-10T12:00:00'}"));

        ResponseEntity<?> responseEntity = roleController.createRole(role);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        String responseBody = responseEntity.getBody().toString();

        // Check if the keys are present in the response
        assertTrue(responseBody.contains("error"));
        assertTrue(responseBody.contains("status"));
        assertTrue(responseBody.contains("timestamp"));

        // Check if the values are present in the response
        assertTrue(responseBody.contains("400"));
        assertTrue(responseBody.contains("An Error Occurred"));
        assertTrue(responseBody.contains("2021-08-10T12:00:00"));

        verify(roleService).createRole(role);
    }

    // Get all Roles Test
    @Test
    void getAllRoles_Success() {
        List<Role> roles = List.of(createTestRole(), createTestRole());

        when(roleService.getAllRoles(anyInt(), anyInt(), anyString())).thenReturn(roles);

        ResponseEntity<?> responseEntity = roleController.getAllRoles(0, 10, "id");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", roles), responseEntity.getBody());
        verify(roleService).getAllRoles(anyInt(), anyInt(), anyString());
    }

    // Get Role By ID Test
    @Test
    void getRoleByID_Success() {
        Role role = createTestRole();

        when(roleService.getRoleById(role.getId())).thenReturn(role);

        ResponseEntity<?> responseEntity = roleController.getRoleById(role.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", role), responseEntity.getBody());
        verify(roleService).getRoleById(role.getId());
    }

    @Test
    void getRoleByID_NotFound() {
        UUID id = createTestUUID();

        when(roleService.getRoleById(id)).thenReturn(null);

        ResponseEntity<?> responseEntity = roleController.getRoleById(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", "Role not found"), responseEntity.getBody());
        verify(roleService).getRoleById(id);
    }

    @Test
    void getRoleByID_Exception() {
        UUID id = UUID.randomUUID();

        when(roleService.getRoleById(id)).thenThrow(
                new RuntimeException("{status: 400, message: 'An Error Occurred', timestamp: '2021-08-10T12:00:00'}"));

        ResponseEntity<?> responseEntity = roleController.getRoleById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        String responseBody = responseEntity.getBody().toString();

        // Check if the keys are present in the response
        assertTrue(responseBody.contains("error"));
        assertTrue(responseBody.contains("status"));
        assertTrue(responseBody.contains("timestamp"));

        // Check if the values are present in the response
        assertTrue(responseBody.contains("400"));
        assertTrue(responseBody.contains("An Error Occurred"));
        assertTrue(responseBody.contains("2021-08-10T12:00:00"));

        verify(roleService).getRoleById(id);
    }

    // Update Role Test
    @Test
    void updateRole_Success() {
        Role role = createTestRole();

        when(roleService.updateRole(role.getId(), role)).thenReturn(role);

        ResponseEntity<?> responseEntity = roleController.updateRole(role.getId(), role);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", role), responseEntity.getBody());
        verify(roleService).updateRole(role.getId(), role);
    }

    @Test
    void updateRole_BadRequest() {
        Role role = createTestRole();

        when(roleService.updateRole(role.getId(), role)).thenReturn(null);

        ResponseEntity<?> responseEntity = roleController.updateRole(role.getId(), role);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", "Unable to process your request"), responseEntity.getBody());
        verify(roleService).updateRole(role.getId(), role);
    }

    @Test
    void updateRole_Exception() {
        Role role = createTestRole();

        when(roleService.updateRole(role.getId(), role)).thenThrow(
                new RuntimeException("{status: 400, message: 'An Error Occurred', timestamp: '2021-08-10T12:00:00'}"));

        ResponseEntity<?> responseEntity = roleController.updateRole(role.getId(), role);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        String responseBody = responseEntity.getBody().toString();

        // Check if the keys are present in the response
        assertTrue(responseBody.contains("error"));
        assertTrue(responseBody.contains("status"));
        assertTrue(responseBody.contains("timestamp"));

        // Check if the values are present in the response
        assertTrue(responseBody.contains("400"));
        assertTrue(responseBody.contains("An Error Occurred"));
        assertTrue(responseBody.contains("2021-08-10T12:00:00"));

        verify(roleService).updateRole(role.getId(), role);
    }

    // Delete Role Test
    @Test
    void deleteRole_Success() {
        Role role = createTestRole();

        when(roleService.deleteRole(role.getId())).thenReturn(role);

        ResponseEntity<?> responseEntity = roleController.deleteRole(role.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", ""), responseEntity.getBody());
        verify(roleService).deleteRole(role.getId());
    }

    @Test
    void deleteRole_NotFound() {
        UUID id = createTestUUID();

        when(roleService.deleteRole(id)).thenReturn(null);

        ResponseEntity<?> responseEntity = roleController.deleteRole(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("data", "Role not found"), responseEntity.getBody());
        verify(roleService).deleteRole(id);
    }

    @Test
    void deleteRole_Exception() {
        UUID id = UUID.randomUUID();

        when(roleService.deleteRole(id)).thenThrow(
                new RuntimeException("{status: 400, message: 'An Error Occurred', timestamp: '2021-08-10T12:00:00'}"));

        ResponseEntity<?> responseEntity = roleController.deleteRole(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        String responseBody = responseEntity.getBody().toString();

        // Check if the keys are present in the response
        assertTrue(responseBody.contains("error"));
        assertTrue(responseBody.contains("status"));
        assertTrue(responseBody.contains("timestamp"));

        // Check if the values are present in the response
        assertTrue(responseBody.contains("400"));
        assertTrue(responseBody.contains("An Error Occurred"));
        assertTrue(responseBody.contains("2021-08-10T12:00:00"));

        verify(roleService).deleteRole(id);
    }
}