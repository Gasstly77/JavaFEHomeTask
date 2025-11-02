package by.javafe.hometask.repository;

import by.javafe.hometask.entity.EmployeeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final EntityManagerFactory entityManagerFactory;

    public EmployeeRepositoryImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("sportsPU");
    }

    @Override
    public List<EmployeeEntity> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT e FROM EmployeeEntity e", EmployeeEntity.class).getResultList();
        }
    }

    @Override
    public void add(EmployeeEntity employee) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(employee);
            transaction.commit();
            System.out.println("Работник добавлен: " + employee);
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении работника: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            EmployeeEntity employee = entityManager.find(EmployeeEntity.class, id);
            if (employee != null) {
                entityManager.remove(employee);
                System.out.println("Работник с ID " + id + " удалён.");
            } else {
                System.out.println("Работник с ID " + id + " не найден.");
            }
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при удалении работника: " + e.getMessage());
        }
    }

    @Override
    public EmployeeEntity findHighestPaidEmployee() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<EmployeeEntity> query = entityManager.createQuery(
                    "SELECT e FROM EmployeeEntity e WHERE e.dismissalDate IS NULL ORDER BY e.monthlySalary DESC",
                    EmployeeEntity.class);
            query.setMaxResults(1);
            List<EmployeeEntity> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        }
    }

    @Override
    public EmployeeEntity findLowestPaidEmployee() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<EmployeeEntity> query = entityManager.createQuery(
                    "SELECT e FROM EmployeeEntity e WHERE e.dismissalDate IS NULL ORDER BY e.monthlySalary ASC",
                    EmployeeEntity.class);
            query.setMaxResults(1);
            List<EmployeeEntity> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        }
    }

    @Override
    public BigDecimal calculateTotalSalaryExpenses(LocalDate startDate, LocalDate endDate) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<EmployeeEntity> employees = entityManager.createQuery(
                    "SELECT e FROM EmployeeEntity e", EmployeeEntity.class).getResultList();

            BigDecimal totalExpenses = BigDecimal.ZERO;

            for (EmployeeEntity employee : employees) {
                LocalDate effectiveStartDate = employee.getHireDate().isAfter(startDate) 
                        ? employee.getHireDate() 
                        : startDate;

                LocalDate effectiveEndDate;
                if (employee.getDismissalDate() != null) {
                    effectiveEndDate = employee.getDismissalDate().isBefore(endDate) 
                            ? employee.getDismissalDate() 
                            : endDate;
                } else {
                    effectiveEndDate = endDate;
                }

                // Проверяем, что период работы пересекается с запрашиваемым периодом
                if (effectiveStartDate.isAfter(effectiveEndDate)) {
                    continue;
                }

                // Вычисляем количество месяцев работы в указанном периоде
                // Нормализуем даты к началу месяцев для корректного расчета
                LocalDate startOfPeriod = effectiveStartDate.withDayOfMonth(1);
                LocalDate endOfPeriod = effectiveEndDate.withDayOfMonth(1);
                
                // Вычисляем разницу в месяцах между нормализованными датами
                Period period = Period.between(startOfPeriod, endOfPeriod);
                long months = period.getYears() * 12L + period.getMonths();
                
                // Если сотрудник работал хотя бы часть месяца (даты не равны), добавляем 1 месяц
                if (!effectiveStartDate.equals(effectiveEndDate)) {
                    months = Math.max(1, months + 1);
                } else if (months == 0) {
                    // Если даты равны, но это означает что сотрудник работал в этом месяце
                    months = 1;
                }

                BigDecimal employeeExpenses = employee.getMonthlySalary().multiply(BigDecimal.valueOf(months));
                totalExpenses = totalExpenses.add(employeeExpenses);
            }

            return totalExpenses;
        }
    }

    @Override
    public List<EmployeeEntity> getAllUsingCriteria() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<EmployeeEntity> query = cb.createQuery(EmployeeEntity.class);
            Root<EmployeeEntity> root = query.from(EmployeeEntity.class);
            query.select(root);
            return entityManager.createQuery(query).getResultList();
        }
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}

