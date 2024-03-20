package com.foodie.user.model.contracts;

import java.util.Objects;

public record RegisterRequest(String firstname, String lastname, String email, String password) {
  public RegisterRequest {
        Objects.requireNonNull(firstname);
        Objects.requireNonNull(lastname);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}