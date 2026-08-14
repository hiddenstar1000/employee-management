package net.dixonai.employeemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.dixonai.employeemanagement.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
