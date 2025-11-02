package by.javafe.hometask.repository;

import by.javafe.hometask.entity.EmployeeEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository {
    List<EmployeeEntity> getAll();

    void add(EmployeeEntity employee);

    void delete(Long id);

    EmployeeEntity findHighestPaidEmployee();

    EmployeeEntity findLowestPaidEmployee();

    BigDecimal calculateTotalSalaryExpenses(LocalDate startDate, LocalDate endDate);
}

