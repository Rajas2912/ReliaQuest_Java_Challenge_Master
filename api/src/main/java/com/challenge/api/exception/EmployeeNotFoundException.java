package com.challenge.api.exception;

import java.util.UUID;

/**
 * Thrown when an employee can't be found. Just a plain exception - the controller catches it and turns it
 * into a 404.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(UUID uuid) {
        super("Employee not found: " + uuid);
    }
}
