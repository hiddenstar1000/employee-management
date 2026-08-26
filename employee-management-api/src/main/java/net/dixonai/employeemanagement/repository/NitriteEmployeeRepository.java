package net.dixonai.employeemanagement.repository;

import net.dixonai.employeemanagement.model.Employee;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("test")
public class NitriteEmployeeRepository implements EmployeeRepository {

    private final Nitrite db;
    private final ObjectRepository<Employee> repository;

    public NitriteEmployeeRepository() {
        this.db = Nitrite.builder().openOrCreate();
        this.repository = db.getRepository(Employee.class);
    }

    @Override
    public <S extends Employee> S save(S entity) {
        if (entity.getId() != null && repository.find(ObjectFilters.eq("id", entity.getId())).totalCount() > 0) {
            repository.update(ObjectFilters.eq("id", entity.getId()), entity);
        } else {
            repository.insert(entity);
        }
        return entity;
    }

    @Override
    public <S extends Employee> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public Optional<Employee> findById(String id) {
        if (id == null) return Optional.empty();
        Employee employee = repository.find(ObjectFilters.eq("id", id)).firstOrDefault();
        return Optional.ofNullable(employee);
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) return false;
        return repository.find(ObjectFilters.eq("id", id)).totalCount() > 0;
    }

    @Override
    public List<Employee> findAll() {
        return repository.find().toList();
    }

    @Override
    public List<Employee> findAllById(Iterable<String> ids) {
        List<Employee> list = new ArrayList<>();
        for (String id : ids) {
            findById(id).ifPresent(list::add);
        }
        return list;
    }

    @Override
    public long count() {
        return repository.find().totalCount();
    }

    @Override
    public void deleteById(String id) {
        if (id != null) {
            repository.remove(ObjectFilters.eq("id", id));
        }
    }

    @Override
    public void delete(Employee entity) {
        if (entity != null && entity.getId() != null) {
            deleteById(entity.getId());
        }
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        for (String id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Employee> entities) {
        for (Employee entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        repository.remove(ObjectFilters.ALL);
    }

    @Override
    public Optional<Employee> findByEmailId(String emailId) {
        if (emailId == null) return Optional.empty();
        Employee employee = repository.find(ObjectFilters.eq("emailId", emailId)).firstOrDefault();
        return Optional.ofNullable(employee);
    }

    @PreDestroy
    public void close() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
    }
}
