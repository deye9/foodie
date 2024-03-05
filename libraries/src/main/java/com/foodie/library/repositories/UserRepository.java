package com.foodie.library.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.foodie.models.users.User;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

}