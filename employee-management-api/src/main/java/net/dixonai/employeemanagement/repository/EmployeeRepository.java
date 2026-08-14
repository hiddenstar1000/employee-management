package net.dixonai.employeemanagement.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import net.dixonai.employeemanagement.model.Employee;

@NoRepositoryBean
public interface EmployeeRepository extends ListCrudRepository<Employee, String> {
}


