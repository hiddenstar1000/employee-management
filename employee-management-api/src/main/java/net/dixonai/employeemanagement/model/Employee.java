package net.dixonai.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Table(name = "employees")
@Document(collection = "employees")
public class Employee {
    
    @Id
    @org.springframework.data.annotation.Id
    private String id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String department;
    private boolean loginEnabled = false;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public Employee() {
    }

    public Employee(String id, String firstName, String lastName, String emailId, String department) {
        this(id, firstName, lastName, emailId, department, false, null);
    }

    public Employee(String id, String firstName, String lastName, String emailId, String department, boolean loginEnabled, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.department = department;
        this.loginEnabled = loginEnabled;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isLoginEnabled() {
        return loginEnabled;
    }

    public void setLoginEnabled(boolean loginEnabled) {
        this.loginEnabled = loginEnabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

