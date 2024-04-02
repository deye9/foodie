package com.foodie.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.foodie.FoodieBaseRepository;
import com.foodie.FoodieBaseService;
import com.foodie.user.model.Role;
import com.foodie.user.repositories.RoleRepository;

@Service
public class RoleService implements FoodieBaseService<Role, UUID> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public FoodieBaseRepository<Role, UUID> getRepository() {
        return roleRepository;
    }

    public Role validateAndGetRole(UUID roleId) {
        return findByIdAndDeletedAtIsNull(roleId).orElse(null);
    }  
}