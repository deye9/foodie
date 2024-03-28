package com.foodie.user.contracts;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest(@NotEmpty String newPassword, @NotEmpty String currentPassword,
        @NotEmpty String confirmationPassword) {}