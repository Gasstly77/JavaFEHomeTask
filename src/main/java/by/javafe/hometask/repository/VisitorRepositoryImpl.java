package by.javafe.hometask.repository;

import by.javafe.hometask.entity.VisitorEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class VisitorRepositoryImpl implements VisitorRepository {
    private final EntityManagerFactory entityManagerFactory;

    public VisitorRepositoryImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("sportsPU");
    }

    @Override
    public List<VisitorEntity> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT v FROM VisitorEntity v", VisitorEntity.class).getResultList();
        }
    }

    @Override
    public void add(VisitorEntity visitor) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(visitor);
            transaction.commit();
            System.out.println("Посетитель добавлен: " + visitor);
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении посетителя: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            VisitorEntity visitor = entityManager.find(VisitorEntity.class, id);
            if (visitor != null) {
                entityManager.remove(visitor);
                System.out.println("Посетитель с ID " + id + " удалён.");
            } else {
                System.out.println("Посетитель с ID " + id + " не найден.");
            }
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при удалении посетителя: " + e.getMessage());
        }
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}

