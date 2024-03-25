package com.foodie.user.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.foodie.FoodieBaseRepository;
import com.foodie.user.model.Permission;

@Repository
public interface PermissionRepository extends FoodieBaseRepository<Permission, UUID> {}
