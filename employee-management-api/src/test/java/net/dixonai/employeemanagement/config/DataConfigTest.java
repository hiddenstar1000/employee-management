package net.dixonai.employeemanagement.config;

import net.dixonai.employeemanagement.repository.EmployeeRepository;
import net.dixonai.employeemanagement.repository.NitriteEmployeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class DataConfigTest {

    @Bean
    public EmployeeRepository employeeRepository() {
        return new NitriteEmployeeRepository();
    }
}

