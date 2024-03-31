package com.foodie.user.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodie.FoodieBaseRepository;
import com.foodie.user.model.UserRole;

import jakarta.transaction.Transactional;

@Repository
public interface UserRoleRepository extends FoodieBaseRepository<UserRole, UUID> {

    @Modifying
    @Transactional
    @Query("update UserRole ur set ur.deletedAt = CURRENT_TIMESTAMP where ur.userId = :userId")
    void deleteAllByUserId(@Param("userId") UUID id);

    @Query("SELECT ur FROM UserRole ur JOIN ur.userId u WHERE u.id = :userId AND ur.deletedAt IS NULL")
    Optional<List<UserRole>> findAllByUserIdAndDeletedAtIsNull(@Param("userId") UUID id);

    @Query("SELECT ur FROM UserRole ur JOIN ur.userId u WHERE u.id = :userId AND ur.deletedAt IS NULL")
    Page<UserRole> findAllByUserIdAndDeletedAtIsNull(@Param("userId") UUID id, Pageable pageable);
}
