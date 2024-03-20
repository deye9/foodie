package com.foodie.exception;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.foodie.ErrorMessageResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorMessageResponse> handleAnyException(Exception ex, WebRequest request) {

        log.error("Handling exception: {}", ex.getMessage());

        String errorMessageDescription = ex.getLocalizedMessage();
        if (errorMessageDescription == null) {
            errorMessageDescription = ex.toString();
        }
        return buildErrorResponse(errorMessageDescription);
    }

    @ExceptionHandler(value = { NullPointerException.class })
    public ResponseEntity<ErrorMessageResponse> handleNullPointerException(NullPointerException ex, WebRequest request) {

        log.error("Null Pointer exception thrown: {}", ex.getMessage());

        ex.printStackTrace();
        String errorMessageDescription = ex.getLocalizedMessage();
        if (errorMessageDescription == null) {
            errorMessageDescription = ex.toString();
        }
        return buildErrorResponse(errorMessageDescription);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    public ResponseEntity<ErrorMessageResponse> handleDataIntegrityValidationException(DataIntegrityViolationException ex, WebRequest request) {

        log.error("Data Integrity exception thrown: {}", ex.getMessage());

        String errorCause = ex.getMessage();
        if (errorCause != null && errorCause.toLowerCase(Locale.ROOT).contains("constraint")) {
            errorCause = "Data already exists";
        } else {
            errorCause = ex.toString();
        }
        return buildErrorResponse(errorCause);
    }

    private ResponseEntity<ErrorMessageResponse> buildErrorResponse(String errorMessageDescription) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse(errorMessageDescription, LocalDateTime.now());
        return new ResponseEntity<ErrorMessageResponse>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
