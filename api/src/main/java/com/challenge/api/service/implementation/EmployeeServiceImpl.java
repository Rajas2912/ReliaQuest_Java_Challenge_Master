package com.challenge.api.service.implementation;

import com.challenge.api.dto.CreateEmployeeRequest;
import com.challenge.api.exception.EmployeeNotFoundException;
import com.challenge.api.model.Employee;
import com.challenge.api.model.MockEmployee;
import com.challenge.api.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * In-memory implementation of EmployeeService. No real database - just a hashmap  with fake employees on
 * startup.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    // ConcurrentHashMap since this is shared across requests
    private final Map<UUID, Employee> employeeStore = new ConcurrentHashMap<>();

    // runs once on startup and fills the store with fake employees
    @PostConstruct
    void seedMockEmployees() {
        String[] firstNames = {"Alex", "Sam", "Jordan", "Taylor", "Casey", "Morgan", "Riley", "Jamie"};
        String[] lastNames = {"Nguyen", "Patel", "Garcia", "Smith", "Johnson", "Brown", "Lee", "Martinez"};
        String[] titles = {"Software Engineer", "Product Manager", "QA Analyst", "DevOps Engineer", "Data Scientist"};

        for (int i = 0; i < 20; i++) {
            String first = firstNames[i % firstNames.length];
            String last = lastNames[(i * 3) % lastNames.length];
            MockEmployee employee = new MockEmployee(
                    UUID.randomUUID(),
                    first,
                    last,
                    50000 + (i * 5000),
                    25 + (i % 15),
                    titles[i % titles.length],
                    (first + "." + last + "@company.com").toLowerCase(),
                    Instant.now().minusSeconds(86400L * 30 * i));
            employeeStore.put(employee.getUuid(), employee);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        // copying so the caller can't modify the internal map
        return List.copyOf(employeeStore.values());
    }

    @Override
    public Employee getEmployeeByUuid(UUID uuid) {
        Employee employee = employeeStore.get(uuid);
        if (employee == null) {
            // plain exception here, controller decides what status code to send back
            throw new EmployeeNotFoundException(uuid);
        }
        return employee;
    }

    @Override
    public Employee createEmployee(CreateEmployeeRequest request) {
        // uuid and hire date get set here, not by the client
        MockEmployee employee = new MockEmployee(
                UUID.randomUUID(),
                request.getFirstName(),
                request.getLastName(),
                request.getSalary(),
                request.getAge(),
                request.getJobTitle(),
                request.getEmail(),
                Instant.now());
        employeeStore.put(employee.getUuid(), employee);
        return employee;
    }
}
