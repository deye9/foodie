package com.foodie.user.contracts;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RolePermitRequest(
        @NotEmpty String id,
        @NotNull PermissionRequest permission) {
}