package com.foodie.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.foodie.FoodieBaseRepository;
import com.foodie.FoodieBaseService;
import com.foodie.user.model.RolePermission;
import com.foodie.user.repositories.RolePermissionRepository;

@Service
public class RolePermissionService implements FoodieBaseService<RolePermission, UUID> {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public FoodieBaseRepository<RolePermission, UUID> getRepository() {
        return rolePermissionRepository;
    }

    public void deleteAllByRoleId(UUID roleId) {
        rolePermissionRepository.findAllByRoleIdAndDeletedAtIsNull(roleId, Pageable.unpaged())
                .forEach(rolePermission -> {
                    rolePermission.setDeletedAt(LocalDateTime.now());
                    rolePermissionRepository.save(rolePermission);
                });
    }

    public Optional<List<RolePermission>> findAllByRoleIdAndDeletedAtIsNull(UUID id) {
        return rolePermissionRepository.findAllByRoleIdAndDeletedAtIsNull(id);
    }

    public Page<RolePermission> findAllByRoleIdAndDeletedAtIsNull(UUID id, Pageable pageable) {
        return rolePermissionRepository.findAllByRoleIdAndDeletedAtIsNull(id, pageable);
    }
}
