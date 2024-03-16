package com.foodie.models.contracts;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(
        Exception error, Object message, HttpStatus status) {

        Map<String, Object> map = new HashMap<String, Object>();

        if (error != null && error.getMessage() != null) {
            map.put("status", status.value());
            map.put("timestamp", LocalDateTime.now());
            map.put("error", Arrays.asList(error.getMessage()));
        }else {
            if (message instanceof Object) {
                map.put("data", message);
            } else {
                map.put("data", Arrays.asList(message));
            }
        }

        // Implement push to slack error channel orother providers i.e Sentry, data dog, etc
        return new ResponseEntity<Object>(map, status);
    }
}