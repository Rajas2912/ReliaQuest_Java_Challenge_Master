package com.challenge.api.service;

import com.challenge.api.dto.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import java.util.List;
import java.util.UUID;

/**
 * Business logic boundary between the controller and the underlying employee data source. Kept as an interface so
 * the mock, in-memory implementation can later be swapped for a real adapter to the legacy system without touching
 * the controller.
 */
public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeByUuid(UUID uuid);

    Employee createEmployee(CreateEmployeeRequest request);
}
