package net.dixonai.employeemanagement.controller;

import net.dixonai.employeemanagement.dto.LoginRequest;
import net.dixonai.employeemanagement.dto.LoginResponse;
import net.dixonai.employeemanagement.model.Employee;
import net.dixonai.employeemanagement.repository.EmployeeRepository;
import net.dixonai.employeemanagement.security.JwtTokenProvider;
import net.dixonai.employeemanagement.service.PasswordEncryptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping({"/auth", "/api/auth"})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final EmployeeRepository employeeRepository;
    private final PasswordEncryptionService passwordEncryptionService;
    private final JwtTokenProvider tokenProvider;

    public AuthController(EmployeeRepository employeeRepository,
                          PasswordEncryptionService passwordEncryptionService,
                          JwtTokenProvider tokenProvider) {
        this.employeeRepository = employeeRepository;
        this.passwordEncryptionService = passwordEncryptionService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmailId() == null || loginRequest.getEmailId().trim().isEmpty() ||
            loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email address and password are required"));
        }

        String inputEmail = loginRequest.getEmailId().trim();
        logger.info("Processing login request for email: {}", inputEmail);

        Optional<Employee> employeeOpt = employeeRepository.findByEmailId(inputEmail);

        if (employeeOpt.isEmpty()) {
            employeeOpt = employeeRepository.findAll().stream()
                    .filter(e -> e.getEmailId() != null && e.getEmailId().trim().equalsIgnoreCase(inputEmail))
                    .findFirst();
        }

        if (employeeOpt.isEmpty()) {
            logger.warn("Login failed: No employee record found for email: {}", inputEmail);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email address or password"));
        }

        Employee employee = employeeOpt.get();

        if (!employee.isLoginEnabled()) {
            logger.warn("Login failed: System login is disabled for email: {}", inputEmail);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "System login is disabled for this account"));
        }

        if (employee.getPassword() == null || !passwordEncryptionService.matches(loginRequest.getPassword(), employee.getPassword())) {
            logger.warn("Login failed: Password mismatch for email: {}", inputEmail);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email address or password"));
        }

        logger.info("Login successful for email: {}", inputEmail);



        String token = tokenProvider.generateToken(employee.getEmailId(), employee.getId(), employee.getDepartment());

        return ResponseEntity.ok(new LoginResponse(token, employee));
    }
}
