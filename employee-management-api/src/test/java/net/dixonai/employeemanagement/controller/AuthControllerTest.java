package net.dixonai.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dixonai.employeemanagement.dto.LoginRequest;
import net.dixonai.employeemanagement.model.Employee;
import net.dixonai.employeemanagement.repository.EmployeeRepository;
import net.dixonai.employeemanagement.security.JwtTokenProvider;
import net.dixonai.employeemanagement.service.PasswordEncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncryptionService passwordEncryptionService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private Employee validEmployee;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        validEmployee = new Employee("1", "John", "Doe", "john@example.com", "Engineering", true, "ENC_GCM_v1:encryptedPass");
    }

    @Test
    void login_Successful_ShouldReturn200AndToken() throws Exception {
        LoginRequest request = new LoginRequest("john@example.com", "password123");
        when(employeeRepository.findByEmailId("john@example.com")).thenReturn(Optional.of(validEmployee));
        when(passwordEncryptionService.matches("password123", "ENC_GCM_v1:encryptedPass")).thenReturn(true);
        when(tokenProvider.generateToken(anyString(), anyString(), anyString())).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("mock-jwt-token")))
                .andExpect(jsonPath("$.employee.emailId", is("john@example.com")));
    }

    @Test
    void login_InvalidPassword_ShouldReturn401() throws Exception {
        LoginRequest request = new LoginRequest("john@example.com", "wrongPass");
        when(employeeRepository.findByEmailId("john@example.com")).thenReturn(Optional.of(validEmployee));
        when(passwordEncryptionService.matches("wrongPass", "ENC_GCM_v1:encryptedPass")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid email address or password")));
    }


    @Test
    void login_DisabledLogin_ShouldReturn401() throws Exception {
        Employee disabledEmployee = new Employee("2", "Jane", "Doe", "jane@example.com", "HR", false, null);
        LoginRequest request = new LoginRequest("jane@example.com", "password123");
        when(employeeRepository.findByEmailId("jane@example.com")).thenReturn(Optional.of(disabledEmployee));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("System login is disabled for this account")));
    }

    @Test
    void login_MissingEmployee_ShouldReturn401() throws Exception {
        LoginRequest request = new LoginRequest("nobody@example.com", "password123");
        when(employeeRepository.findByEmailId("nobody@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid email address or password")));
    }
}
