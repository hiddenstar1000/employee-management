package net.dixonai.employeemanagement.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import net.dixonai.employeemanagement.model.Employee;

@Repository
@Profile("!test")
public interface MongoEmployeeRepository extends MongoRepository<Employee, String>, EmployeeRepository {
}
