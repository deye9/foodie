package com.foodie;

import java.time.LocalDateTime;

/**
 * Custom error message class
 **/
public record FoodieBaseErrorResponse(Boolean error, String message, LocalDateTime errorDate) {
    public FoodieBaseErrorResponse(String message, LocalDateTime errorDate) {
        this(true, message, errorDate);
    }
}