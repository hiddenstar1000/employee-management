package net.dixonai.employeemanagement.dto;

import net.dixonai.employeemanagement.model.Employee;

public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private Employee employee;

    public LoginResponse() {
    }

    public LoginResponse(String token, Employee employee) {
        this.token = token;
        this.employee = employee;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
