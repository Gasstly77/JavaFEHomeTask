package by.javafe.hometask.repository;

import by.javafe.hometask.entity.EmployeeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

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

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}

