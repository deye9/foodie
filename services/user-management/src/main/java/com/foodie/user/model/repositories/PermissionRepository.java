package com.foodie.user.model.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.foodie.user.model.Permission;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByPermissionName(String permissionName);
}
