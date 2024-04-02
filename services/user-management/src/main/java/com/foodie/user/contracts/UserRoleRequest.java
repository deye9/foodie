package com.foodie.user.contracts;

import com.foodie.user.model.Role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserRoleRequest(
        @NotEmpty String id,
        @NotNull Role role) {
}