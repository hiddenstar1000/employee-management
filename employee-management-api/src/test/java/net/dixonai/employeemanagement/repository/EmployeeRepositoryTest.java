package net.dixonai.employeemanagement.repository;

import net.dixonai.employeemanagement.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void save_ShouldPersistEmployeeToH2Database() {
        Employee employee = new Employee("emp-1", "Alice", "Johnson", "alice@example.com", "Finance");

        Employee saved = employeeRepository.save(employee);

        assertNotNull(saved);
        assertEquals("emp-1", saved.getId());
        assertEquals("Alice", saved.getFirstName());
    }

    @Test
    void findById_ShouldReturnEmployeeFromH2Database() {
        Employee employee = new Employee("emp-2", "Bob", "Williams", "bob@example.com", "Marketing");
        employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findById("emp-2");

        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getFirstName());
        assertEquals("Marketing", found.get().getDepartment());
    }

    @Test
    void findAll_ShouldReturnAllEmployeesInH2Database() {
        Employee e1 = new Employee("emp-3", "Charlie", "Brown", "charlie@example.com", "Sales");
        Employee e2 = new Employee("emp-4", "Diana", "Prince", "diana@example.com", "Legal");
        employeeRepository.save(e1);
        employeeRepository.save(e2);

        List<Employee> employees = employeeRepository.findAll();

        assertTrue(employees.size() >= 2);
    }

    @Test
    void deleteById_ShouldRemoveEmployeeFromH2Database() {
        Employee employee = new Employee("emp-5", "Eve", "Adams", "eve@example.com", "Operations");
        employeeRepository.save(employee);

        employeeRepository.deleteById("emp-5");

        Optional<Employee> found = employeeRepository.findById("emp-5");
        assertFalse(found.isPresent());
    }
}
