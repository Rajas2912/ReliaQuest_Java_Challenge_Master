package com.challenge.api.exception;

import java.util.UUID;

/**
 * Thrown by the service layer when a requested employee doesn't exist. Deliberately a plain RuntimeException with
 * no HTTP awareness — translating this into a status code is the controller's job, not the service's.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(UUID uuid) {
        super("Employee not found: " + uuid);
    }
}
