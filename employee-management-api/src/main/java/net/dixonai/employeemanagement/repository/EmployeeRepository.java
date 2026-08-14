package net.dixonai.employeemanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import net.dixonai.employeemanagement.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
