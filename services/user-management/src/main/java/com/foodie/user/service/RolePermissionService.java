package com.foodie.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.foodie.FoodieBaseRepository;
import com.foodie.FoodieBaseService;
import com.foodie.user.model.RolePermission;
import com.foodie.user.repositories.RolePermissionRepository;

@Service
public class RolePermissionService implements FoodieBaseService<RolePermission, UUID>{

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

     @Override
    public FoodieBaseRepository<RolePermission, UUID> getRepository() {
        return rolePermissionRepository;
    }    
}
