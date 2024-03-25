package com.foodie;

public record FoodieBaseResponse(Object data) {
    public FoodieBaseResponse {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
    }
}