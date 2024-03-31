package com.foodie.user.contracts;

import jakarta.validation.constraints.NotEmpty;

public record RegisterRequest(@NotEmpty String firstname, @NotEmpty String lastname, @NotEmpty String email,
    @NotEmpty String password) {
}