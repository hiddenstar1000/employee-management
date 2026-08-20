package net.dixonai.employeemanagement.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.dixonai.employeemanagement.model.Employee;
import net.dixonai.employeemanagement.service.EmployeeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping({"/employees", "/api/employees"})
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        logger.info("REST request to create Employee: emailId={}, department={}", employee.getEmailId(), employee.getDepartment());
        Employee created = employeeService.createEmployee(employee);
        logger.info("Successfully created Employee with id={}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("REST request to get all Employees");
        List<Employee> employees = employeeService.getAllEmployees();
        logger.info("Found {} employee records", employees.size());
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        logger.info("REST request to get Employee by id={}", id);
        return employeeService.getEmployeeById(id)
                .map(employee -> {
                    logger.info("Found Employee for id={}", id);
                    return ResponseEntity.ok(employee);
                })
                .orElseGet(() -> {
                    logger.warn("Employee not found for id={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee employeeDetails) {
        logger.info("REST request to update Employee for id={}", id);
        try {
            Employee updated = employeeService.updateEmployee(id, employeeDetails);
            logger.info("Successfully updated Employee for id={}", id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.warn("Failed to update Employee for id={}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        logger.info("REST request to delete Employee for id={}", id);
        employeeService.deleteEmployee(id);
        logger.info("Successfully deleted Employee for id={}", id);
        return ResponseEntity.noContent().build();
    }
}

