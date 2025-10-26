package by.javafe.hometask.service;

import by.javafe.hometask.entity.EmployeeEntity;
import by.javafe.hometask.repository.EmployeeRepositoryImpl;

import java.util.List;

public class EmployeeService {
    private final EmployeeRepositoryImpl employeeRepository;

    public EmployeeService() {
        this.employeeRepository = new EmployeeRepositoryImpl();
    }

    public void addEmployee(EmployeeEntity employee) {
        employeeRepository.add(employee);
    }

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.getAll();
    }

    public void deleteEmployee(Long id) {
        employeeRepository.delete(id);
    }

    public void close() {
        employeeRepository.close();
    }
}

