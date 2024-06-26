package com.foodie.user.contracts;

import com.foodie.user.model.Permission;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RolePermitRequest(
        @NotEmpty String id,
        @NotNull Permission permission) {
}