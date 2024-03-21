package com.foodie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Custom error message class
 **/
@Getter
@Setter
@NoArgsConstructor
public class FoodieBaseErrorResponse {

    private Boolean error;
    private String message;
    private LocalDateTime errorDate;

    public FoodieBaseErrorResponse(String message, LocalDateTime errorDate) {
        this.error = true;
        this.message = message;
        this.errorDate = errorDate;
    }

}