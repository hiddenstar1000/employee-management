package com.example.employeemanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.employeemanagement.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
