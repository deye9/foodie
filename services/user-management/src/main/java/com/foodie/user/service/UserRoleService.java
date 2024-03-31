package com.foodie.user.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.foodie.FoodieBaseRepository;
import com.foodie.FoodieBaseService;
import com.foodie.user.model.UserRole;
import com.foodie.user.repositories.UserRoleRepository;

@Service
public class UserRoleService implements FoodieBaseService<UserRole, UUID> {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public FoodieBaseRepository<UserRole, UUID> getRepository() {
        return userRoleRepository;
    }

    public void deleteAllByUserId(UUID id) {
        userRoleRepository.deleteAllByUserId(id);
    }

    public Optional<List<UserRole>> findAllByUserIdAndDeletedAtIsNull(UUID id) {
        return userRoleRepository.findAllByUserIdAndDeletedAtIsNull(id);
    }

    public Page<UserRole> findAllByUserIdAndDeletedAtIsNull(UUID id, Pageable pageable) {
        return userRoleRepository.findAllByUserIdAndDeletedAtIsNull(id, pageable);
    }
}
