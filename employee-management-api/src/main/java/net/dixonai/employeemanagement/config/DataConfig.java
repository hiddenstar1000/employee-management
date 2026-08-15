package net.dixonai.employeemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("!test")
@EnableMongoRepositories(basePackages = "net.dixonai.employeemanagement.repository")
public class DataConfig {
}

