package com.foodie.user.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.foodie.FoodieBaseRepository;
import com.foodie.user.model.User;

@Repository
public interface UserRepository extends FoodieBaseRepository<User, UUID> {
  Optional<User> findByEmail(String email);
}
