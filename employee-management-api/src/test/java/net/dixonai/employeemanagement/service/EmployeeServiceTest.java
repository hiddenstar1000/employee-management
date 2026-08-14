package net.dixonai.employeemanagement.service;

import net.dixonai.employeemanagement.model.Employee;
import net.dixonai.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee("1", "John", "Doe", "john.doe@example.com", "Engineering");
        employee2 = new Employee("2", "Jane", "Smith", "jane.smith@example.com", "HR");
    }

    @Test
    void createEmployee_ShouldReturnSavedEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        Employee created = employeeService.createEmployee(employee1);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        verify(employeeRepository, times(1)).save(employee1);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_WhenFound_ShouldReturnEmployee() {
        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee1));

        Optional<Employee> found = employeeService.getEmployeeById("1");

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        verify(employeeRepository, times(1)).findById("1");
    }

    @Test
    void getEmployeeById_WhenNotFound_ShouldReturnEmpty() {
        when(employeeRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Employee> found = employeeService.getEmployeeById("99");

        assertFalse(found.isPresent());
        verify(employeeRepository, times(1)).findById("99");
    }

    @Test
    void updateEmployee_WhenFound_ShouldUpdateAndReturnEmployee() {
        Employee updatedDetails = new Employee(null, "Johnathan", "Doe", "john.doe@example.com", "Management");
        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee updated = employeeService.updateEmployee("1", updatedDetails);

        assertNotNull(updated);
        assertEquals("Johnathan", updated.getFirstName());
        assertEquals("Management", updated.getDepartment());
        verify(employeeRepository, times(1)).findById("1");
        verify(employeeRepository, times(1)).save(employee1);
    }

    @Test
    void updateEmployee_WhenNotFound_ShouldThrowException() {
        Employee updatedDetails = new Employee(null, "Johnathan", "Doe", "john.doe@example.com", "Management");
        when(employeeRepository.findById("99")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                employeeService.updateEmployee("99", updatedDetails));

        assertEquals("Employee not found with id 99", exception.getMessage());
        verify(employeeRepository, times(1)).findById("99");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteEmployee_ShouldCallRepositoryDeleteById() {
        doNothing().when(employeeRepository).deleteById("1");

        employeeService.deleteEmployee("1");

        verify(employeeRepository, times(1)).deleteById("1");
    }
}
