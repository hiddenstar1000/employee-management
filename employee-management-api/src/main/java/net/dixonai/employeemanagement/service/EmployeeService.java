package net.dixonai.employeemanagement.service;

import org.springframework.stereotype.Service;
import net.dixonai.employeemanagement.model.Employee;
import net.dixonai.employeemanagement.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {
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
                    return employeeRepository.save(existingEmployee);
                }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }
}
