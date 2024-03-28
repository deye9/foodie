package com.foodie.user.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.foodie.FoodieBaseRepository;
import com.foodie.user.model.RolePermission;

@Repository
public interface RolePermissionRepository extends FoodieBaseRepository<RolePermission, UUID> {}
