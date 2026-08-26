package net.dixonai.employeemanagement.repository;

import net.dixonai.employeemanagement.EmployeeManagementApiApplication;
import net.dixonai.employeemanagement.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import net.dixonai.employeemanagement.config.DataConfigTest;

@SpringBootTest(classes = {EmployeeManagementApiApplication.class, DataConfigTest.class})
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void save_ShouldPersistEmployeeToNitriteDatabase() {
        Employee employee = new Employee("emp-1", "Alice", "Johnson", "alice@example.com", "Finance");

        Employee saved = employeeRepository.save(employee);

        assertNotNull(saved);
        assertEquals("emp-1", saved.getId());
        assertEquals("Alice", saved.getFirstName());
    }

    @Test
    void findById_ShouldReturnEmployeeFromNitriteDatabase() {
        Employee employee = new Employee("emp-2", "Bob", "Williams", "bob@example.com", "Marketing");
        employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findById("emp-2");

        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getFirstName());
        assertEquals("Marketing", found.get().getDepartment());
    }

    @Test
    void findAll_ShouldReturnAllEmployeesInNitriteDatabase() {
        Employee e1 = new Employee("emp-3", "Charlie", "Brown", "charlie@example.com", "Sales");
        Employee e2 = new Employee("emp-4", "Diana", "Prince", "diana@example.com", "Legal");
        employeeRepository.save(e1);
        employeeRepository.save(e2);

        List<Employee> employees = employeeRepository.findAll();

        assertEquals(2, employees.size());
    }

    @Test
    void deleteById_ShouldRemoveEmployeeFromNitriteDatabase() {
        Employee employee = new Employee("emp-5", "Eve", "Adams", "eve@example.com", "Operations");
        employeeRepository.save(employee);

        employeeRepository.deleteById("emp-5");

        Optional<Employee> found = employeeRepository.findById("emp-5");
        assertFalse(found.isPresent());
    }

    @Test
    void findByEmailId_ShouldReturnEmployeeFromNitriteDatabase() {
        Employee employee = new Employee("emp-6", "Frank", "Castle", "frank@example.com", "Security");
        employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findByEmailId("frank@example.com");

        assertTrue(found.isPresent());
        assertEquals("Frank", found.get().getFirstName());
    }
}
