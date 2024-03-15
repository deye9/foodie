package com.foodie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.foodie.models.repositories.RoleRepository;
import com.foodie.models.users.Role;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role deleteRole(UUID id) {

        Optional<Role> retrievedRole = roleRepository.findByIdAndDeletedAtIsNull(id);

        if (retrievedRole.isEmpty()) {
            return null;
        }

        Role role = retrievedRole.get();
        // role.setDeletedAt(LocalDateTime.now());

        return roleRepository.save(role);
    }

    public Role getRoleById(UUID id) {

        Optional<Role> retrievedRole = roleRepository.findByIdAndDeletedAtIsNull(id);

        return retrievedRole.orElse(null);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(UUID id, Role role) {

        Optional<Role> retrievedRole = roleRepository.findByIdAndDeletedAtIsNull(id);

        if (retrievedRole.isEmpty()) return null;

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

        if (rolePage.hasContent()) {
            return rolePage.getContent();
        } else {
            return List.of();
        }
    }
}
