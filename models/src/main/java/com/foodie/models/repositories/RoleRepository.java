package com.foodie.models.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.foodie.models.users.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(String roleName);

    Optional<Role> findByIdAndDeletedAtIsNull(UUID id);

    Page<Role> findAllByDeletedAtIsNull(Pageable pageable);
}
