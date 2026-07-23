package com.challenge.api.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.api.dto.CreateEmployeeRequest;
import com.challenge.api.dto.EmployeeResponse;
import com.challenge.api.exception.EmployeeNotFoundException;
import com.challenge.api.exception.ErrorResponse;
import com.challenge.api.model.Employee;
import com.challenge.api.service.EmployeeService;

import jakarta.validation.Valid;

/**
 * Exposes employee data as a REST API for consumption by Employees-R-US web hooks. Delegates business logic to
 * {@link EmployeeService} and owns all HTTP-facing concerns: request/response shape and exception-to-status-code
 * translation.
 */
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * @return All employees, unfiltered.
     */
    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees().stream()
                .map(EmployeeResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * @param uuid Employee UUID
     * @return Requested Employee if it exists; 404 otherwise, via {@link #handleNotFound}.
     */
    @GetMapping("/{uuid}")
    public EmployeeResponse getEmployeeByUuid(@PathVariable UUID uuid) {
        Employee employee = employeeService.getEmployeeByUuid(uuid);
        return new EmployeeResponse(employee);
    }

    /**
     * @param requestBody Attributes necessary to create an employee.
     * @return Newly created Employee with a 201 status.
     */
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest requestBody) {
        Employee created = employeeService.createEmployee(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EmployeeResponse(created));
    }

    /**
     * Translates a missing employee into a 404. The service layer stays unaware of HTTP status codes.
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EmployeeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Translates a failed @Valid check on the create request into a 400 with a readable message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
    }

    /**
     * Catch-all so an unexpected failure returns a clean 500 instead of a stack trace.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unexpected error occurred"));
    }
}
