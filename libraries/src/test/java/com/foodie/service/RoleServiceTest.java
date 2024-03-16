package com.foodie.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.foodie.models.repositories.RoleRepository;
import com.foodie.models.users.Role;
import com.github.javafaker.Faker;

@RunWith(MockitoJUnitRunner.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Faker faker = new Faker();
        Role userRole = new Role(faker.name().username(), faker.book().toString());
        Role adminRole = new Role(faker.name().username(), faker.book().toString());
    }

    // // Get Role By ID
    // @Test
    // void test_GetRoleById_Success() {

    //     when(roleRepository.findByIdAndDeletedAtIsNull(userRole.getId())).thenReturn(Optional.of(userRole));
    //     when(roleRepository.findByIdAndDeletedAtIsNull(adminRole.getId())).thenReturn(Optional.of(adminRole));

    //     assertEquals(userRole, roleService.getRoleById(userRole.getId()));
    //     assertEquals(adminRole, roleService.getRoleById(adminRole.getId()));
    // }

    @Test
    void test_GetRoleById_NotFound() {

        UUID id = UUID.randomUUID();
        Role role = new Role();
        role.setId(id);

        when(roleRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(role));

        Role result = roleService.getRoleById(id);

        assertEquals(role, result);
    }

    @Test
    void test_GetRoleByIdNotFound() {

        UUID id = UUID.randomUUID();

        when(roleRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

        Role result = roleService.getRoleById(id);

        assertNull(result);
    }

    // Get all roles
    @Test
    void test_GetAllRoles_NullParameters_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> roleService.getAllRoles(null, null, null));
    }

    // @Test
    // void test_GetAllRoles_NullRolePage_ThrowsException() {
    //     Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
    //     when(roleRepository.findAllByDeletedAtIsNull(pageable)).thenReturn(null);
    //     assertThrows(IllegalArgumentException.class, () -> roleService.getAllRoles(0, 10, "name"));
    // }
    
    @Test
    void test_GetAllRoles_NoContent_ReturnsEmptyList() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Role> rolePage = Mockito.mock(Page.class);

        when(rolePage.hasContent()).thenReturn(false);
        when(roleRepository.findAllByDeletedAtIsNull(pageable)).thenReturn(rolePage);
        
        List<Role> roles = roleService.getAllRoles(0, 10, "name");
        assertTrue(roles.isEmpty());
    }

    @Test
    void test_GetAllRoles_HasContent_ReturnsContent() {
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Role> rolePage = Mockito.mock(Page.class);
        
        when(rolePage.hasContent()).thenReturn(true);
        List<Role> expectedRoles = List.of(new Role(), new Role());
        
        when(rolePage.getContent()).thenReturn(expectedRoles);
        when(roleRepository.findAllByDeletedAtIsNull(pageable)).thenReturn(rolePage);
        
        List<Role> roles = roleService.getAllRoles(0, 10, "name");
        assertEquals(expectedRoles, roles);
    }

    // Create Role
    @Test
    void test_CreateRole_Success() {

        Role role = new Role();
        
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.createRole(role);
        assertEquals(role, result);
    }

    // Update Role
    @Test
    void test_UpdateRole_Success() {

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setRoleName("User");
        role.setRoleDescription("User Role");

        when(roleRepository.findByIdAndDeletedAtIsNull(role.getId())).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.updateRole(role.getId(), role);
        assertEquals(role, result);
    }

    @Test
    void test_UpdateRole_NotFound() {

        UUID id = UUID.randomUUID();
        Role role = new Role();
        role.setId(id);

        when(roleRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(role));

        Role result = roleService.updateRole(id, role);

        assertNull(result);
    }

    // // Delete Role
    // @Test
    // void test_DeleteRole_Success() {

    //     Role role = new Role();
    //     role.setId(UUID.randomUUID());

    //     when(roleRepository.findByIdAndDeletedAtIsNull(role.getId())).thenReturn(Optional.of(role));
    //     when(roleRepository.save(role)).thenReturn(role);

    //     Role result = roleService.deleteRole(role.getId());
    //     assertNotNull(result.getDeletedAt());
    // }

    @Test
    void test_DeleteRole_NotFound() {

        UUID id = UUID.randomUUID();
        Role role = new Role();
        role.setId(id);

        when(roleRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(role));

        Role result = roleService.deleteRole(id);

        assertNull(result);
    }
}
