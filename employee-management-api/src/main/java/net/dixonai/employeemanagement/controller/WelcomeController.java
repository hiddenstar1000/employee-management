package net.dixonai.employeemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public String getWelcomeMessage() {
        return "Employee Management API is up and running ...";
    }
}
