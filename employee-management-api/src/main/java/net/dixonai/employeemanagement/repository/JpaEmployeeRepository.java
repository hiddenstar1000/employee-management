package net.dixonai.employeemanagement.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.dixonai.employeemanagement.model.Employee;

@Repository
@Profile("test")
public interface JpaEmployeeRepository extends JpaRepository<Employee, String>, EmployeeRepository {
}
