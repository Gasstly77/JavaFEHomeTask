package by.javafe.hometask.service;

import by.javafe.hometask.entity.EmployeeEntity;
import by.javafe.hometask.repository.EmployeeRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public EmployeeEntity findHighestPaidEmployee() {
        return employeeRepository.findHighestPaidEmployee();
    }

    public EmployeeEntity findLowestPaidEmployee() {
        return employeeRepository.findLowestPaidEmployee();
    }

    public BigDecimal calculateTotalSalaryExpenses(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.calculateTotalSalaryExpenses(startDate, endDate);
    }

    public List<EmployeeEntity> getAllUsingCriteria() {
        return employeeRepository.getAllUsingCriteria();
    }

    public void close() {
        employeeRepository.close();
    }
}

