package com.foodie.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import com.foodie.FoodieBaseErrorResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AppExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private AppExceptionHandler appExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleAnyException() {
        Exception ex = new Exception("Test Exception");
        when(webRequest.getDescription(false)).thenReturn("Test description");

        ResponseEntity<FoodieBaseErrorResponse> responseEntity = appExceptionHandler.handleAnyException(ex, webRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Test Exception", responseEntity.getBody().message());
        assertEquals(LocalDateTime.now().toLocalDate(), responseEntity.getBody().errorDate().toLocalDate());
        assertEquals(LocalDateTime.now().getDayOfYear(), responseEntity.getBody().errorDate().getDayOfYear());
    }

    @Test
    public void testHandleNullPointerException() {
        NullPointerException ex = new NullPointerException("Test Null Pointer Exception");
        when(webRequest.getDescription(false)).thenReturn("Test Null Pointer Exception");

        ResponseEntity<FoodieBaseErrorResponse> responseEntity = appExceptionHandler.handleNullPointerException(ex, webRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Test Null Pointer Exception", responseEntity.getBody().message());
        assertEquals(LocalDateTime.now().toLocalDate(), responseEntity.getBody().errorDate().toLocalDate());
        assertEquals(LocalDateTime.now().getDayOfYear(), responseEntity.getBody().errorDate().getDayOfYear());
    }

    @Test
    public void testHandleDataIntegrityValidationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Test Data Integrity Exception");
        when(webRequest.getDescription(false)).thenReturn("Test Data Integrity Exception");

        ResponseEntity<FoodieBaseErrorResponse> responseEntity = appExceptionHandler.handleDataIntegrityValidationException(ex, webRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Test Data Integrity Exception", responseEntity.getBody().message().split(": ")[1]);
        assertEquals(LocalDateTime.now().toLocalDate(), responseEntity.getBody().errorDate().toLocalDate());
        assertEquals(LocalDateTime.now().getDayOfYear(), responseEntity.getBody().errorDate().getDayOfYear());
    }

}
