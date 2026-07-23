package com.challenge.api.exception;

import java.time.Instant;

/**
 * Consistent error body returned by every {@code @ExceptionHandler} in {@code EmployeeController}.
 */
public class ErrorResponse {

    private final String message;
    private final Instant timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
