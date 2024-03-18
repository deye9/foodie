package com.foodie;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Custom error message class
 **/
@Data
@NoArgsConstructor
public class ErrorMessageResponse {

    private Boolean error;
    private String message;
    private LocalDateTime errorDate;

    public ErrorMessageResponse(String message, LocalDateTime errorDate) {
        this.error = true;
        this.message = message;
        this.errorDate = errorDate;
    }

}