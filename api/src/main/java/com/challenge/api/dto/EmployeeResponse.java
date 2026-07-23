package com.challenge.api.dto;

import com.challenge.api.model.Employee;
import java.time.Instant;
import java.util.UUID;

/**
 * Public API representation of an employee, kept separate from {@link Employee} so the internal domain model can
 * evolve (e.g. once a real adapter to the legacy system replaces {@code MockEmployee}) without changing the
 * contract Employees-R-US depends on.
 */
public class EmployeeResponse {

    private final UUID uuid;
    private final String firstName;
    private final String lastName;
    private final String fullName;
    private final Integer salary;
    private final Integer age;
    private final String jobTitle;
    private final String email;
    private final Instant contractHireDate;

    public EmployeeResponse(Employee employee) {
        this.uuid = employee.getUuid();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.fullName = employee.getFullName();
        this.salary = employee.getSalary();
        this.age = employee.getAge();
        this.jobTitle = employee.getJobTitle();
        this.email = employee.getEmail();
        this.contractHireDate = employee.getContractHireDate();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getSalary() {
        return salary;
    }

    public Integer getAge() {
        return age;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public Instant getContractHireDate() {
        return contractHireDate;
    }
}
