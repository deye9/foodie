package com.foodie.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.foodie.FoodieBaseRepository;
import com.foodie.FoodieBaseService;
import com.foodie.user.model.Permission;
import com.foodie.user.repositories.PermissionRepository;

@Service
public class PermissionService implements FoodieBaseService<Permission, UUID>{

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

     @Override
    public FoodieBaseRepository<Permission, UUID> getRepository() {
        return permissionRepository;
    }    
}
