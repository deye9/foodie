package com.foodie.user.contracts;

import java.util.Objects;

public record ChangePasswordRequest(String newPassword, String currentPassword, String confirmationPassword) {
    public ChangePasswordRequest {
        Objects.requireNonNull(newPassword);
        Objects.requireNonNull(currentPassword);
        Objects.requireNonNull(confirmationPassword);
    }
}