package net.dixonai.employeemanagement.service;

import org.springframework.stereotype.Service;
import net.dixonai.employeemanagement.model.Employee;
import net.dixonai.employeemanagement.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncryptionService passwordEncryptionService;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncryptionService passwordEncryptionService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getId() == null || employee.getId().trim().isEmpty()) {
            employee.setId(UUID.randomUUID().toString());
        }

        processPassword(employee, employee.getPassword());
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(String id, Employee employeeDetails) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    existingEmployee.setFirstName(employeeDetails.getFirstName());
                    existingEmployee.setLastName(employeeDetails.getLastName());
                    existingEmployee.setEmailId(employeeDetails.getEmailId());
                    existingEmployee.setDepartment(employeeDetails.getDepartment());
                    existingEmployee.setLoginEnabled(employeeDetails.isLoginEnabled());

                    if (employeeDetails.isLoginEnabled()) {
                        String newPassword = employeeDetails.getPassword();
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            existingEmployee.setPassword(encryptPassword(newPassword));
                        } else if (existingEmployee.getPassword() == null || existingEmployee.getPassword().trim().isEmpty()) {
                            throw new IllegalArgumentException("Password is required when login is enabled");
                        }
                    } else {
                        existingEmployee.setPassword(null);
                    }

                    return employeeRepository.save(existingEmployee);
                }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    private void processPassword(Employee employee, String rawPassword) {
        if (employee.isLoginEnabled()) {
            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required when login is enabled");
            }
            employee.setPassword(encryptPassword(rawPassword));
        } else {
            employee.setPassword(null);
        }
    }

    private String encryptPassword(String password) {
        if (passwordEncryptionService != null) {
            return passwordEncryptionService.encrypt(password);
        }
        return password;
    }
}

