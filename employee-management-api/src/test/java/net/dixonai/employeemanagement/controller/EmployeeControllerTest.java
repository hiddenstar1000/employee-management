package net.dixonai.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dixonai.employeemanagement.model.Employee;
import net.dixonai.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper;
    private Employee employee1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();
        employee1 = new Employee("1", "John", "Doe", "john.doe@example.com", "Engineering");
    }

    @Test
    void createEmployee_ShouldReturn200AndCreatedEmployee() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee1);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() throws Exception {
        Employee employee2 = new Employee("2", "Jane", "Smith", "jane.smith@example.com", "HR");
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    void getEmployeeById_WhenFound_ShouldReturn200AndEmployee() throws Exception {
        when(employeeService.getEmployeeById("1")).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    void getEmployeeById_WhenNotFound_ShouldReturn404() throws Exception {
        when(employeeService.getEmployeeById("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployee_WhenFound_ShouldReturn200AndUpdatedEmployee() throws Exception {
        when(employeeService.updateEmployee(eq("1"), any(Employee.class))).thenReturn(employee1);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    void updateEmployee_WhenNotFound_ShouldReturn404() throws Exception {
        when(employeeService.updateEmployee(eq("99"), any(Employee.class)))
                .thenThrow(new RuntimeException("Employee not found with id 99"));

        mockMvc.perform(put("/employees/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployee_ShouldReturn204NoContent() throws Exception {
        doNothing().when(employeeService).deleteEmployee("1");

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee("1");
    }
}
