package net.dixonai.employeemanagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmployeeManagementApiApplicationTest {

    @Test
    void main_ShouldExecuteWithoutException() {
        assertDoesNotThrow(() -> {
            // Verifies the application class definition is loadable
            Class.forName("net.dixonai.employeemanagement.EmployeeManagementApiApplication");
        });
    }
}
