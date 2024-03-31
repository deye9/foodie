package com.foodie.user.contracts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AuthenticationRequest(@Email String email, @NotEmpty String password) {}
