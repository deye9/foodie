package com.foodie.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.foodie.models.repositories.RoleRepository;
import com.foodie.models.users.Role;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role deleteRole(UUID id) {

        Optional<Role> retrievedRole = roleRepository.findByIdAndDeletedAtIsNull(id);

        if (!retrievedRole.isPresent()) {
            return null;
        }

        Role role = retrievedRole.get();
        role.setDeletedAt(LocalDateTime.now());

        return roleRepository.save(role);
    }

    public Role getRoleById(UUID id) {

        Optional<Role> retrievedRole = roleRepository.findByIdAndDeletedAtIsNull(id);

        if (!retrievedRole.isPresent()) {
            return null;
        }

        return retrievedRole.get();
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(UUID id, Role role) {

        Optional<Role> retrievedRole = roleRepository.findByIdAndDeletedAtIsNull(id);

        if (!retrievedRole.isPresent()) {
            return null;
        }

        Role newRole = retrievedRole.get();
        newRole.setRoleName(role.getRoleName());
        newRole.setRoleDescription(role.getRoleDescription());

        newRole = roleRepository.save(newRole);

        return newRole;
    }

    public List<Role> getAllRoles(Integer page, Integer size, String sort) {
        
        if (page == null || size == null || sort == null) {
            throw new IllegalArgumentException("Page, size, and sort parameters are required");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<Role> rolePage = roleRepository.findAllByDeletedAtIsNull(pageable);
        if (rolePage == null) {
            throw new IllegalArgumentException("RolePage returned null");
        }

        if (rolePage.hasContent()) {
            return rolePage.getContent();
        } else {
            return List.of();
        }
    }

}
