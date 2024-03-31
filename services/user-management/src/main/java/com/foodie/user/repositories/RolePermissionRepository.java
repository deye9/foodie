package com.foodie.user.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.foodie.FoodieBaseRepository;
import com.foodie.user.model.RolePermission;

@Repository
public interface RolePermissionRepository extends FoodieBaseRepository<RolePermission, UUID> {
    void deleteAllByRoleId(UUID roleId);
    Optional<List<RolePermission>> findAllByRoleIdAndDeletedAtIsNull(UUID id);
    Page<RolePermission> findAllByRoleIdAndDeletedAtIsNull(UUID id, Pageable pageable);
}
