package com.foodie.user.model.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.foodie.FoodieBaseRepository;
import com.foodie.user.model.Role;

@Repository
public interface RoleRepository extends FoodieBaseRepository<Role, UUID> {
}
