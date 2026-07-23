package com.challenge.api.service;

import com.challenge.api.dto.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import java.util.List;
import java.util.UUID;

/**
 * Business logic for employees, kept as an interface so the mock data source can be swapped out later without
 * touching the controller.
 */
public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeByUuid(UUID uuid);

    Employee createEmployee(CreateEmployeeRequest request);
}
