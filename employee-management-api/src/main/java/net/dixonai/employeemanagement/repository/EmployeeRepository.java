package net.dixonai.employeemanagement.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import net.dixonai.employeemanagement.model.Employee;

import java.util.Optional;

@NoRepositoryBean
public interface EmployeeRepository extends ListCrudRepository<Employee, String> {
    Optional<Employee> findByEmailId(String emailId);
}



