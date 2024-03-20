package com.foodie.user.model.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodie.FoodieBaseRepositoryImpl;
import com.foodie.user.model.Role;
import com.foodie.user.model.repositories.RoleRepository;
import com.foodie.user.model.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl extends FoodieBaseRepositoryImpl<Role, UUID> implements RoleService {

    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
    }
}
