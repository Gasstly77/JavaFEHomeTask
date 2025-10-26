package by.javafe.hometask.repository;

import by.javafe.hometask.entity.EmployeeEntity;

import java.util.List;

public interface EmployeeRepository {
    List<EmployeeEntity> getAll();

    void add(EmployeeEntity employee);

    void delete(Long id);
}

