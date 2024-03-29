package com.foodie.user.contracts;

import java.util.Objects;

public record AuthenticationRequest(String email, String password) {
  public AuthenticationRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}