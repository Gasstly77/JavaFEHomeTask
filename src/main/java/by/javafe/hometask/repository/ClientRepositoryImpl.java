package by.javafe.hometask.repository;

import by.javafe.hometask.constant.ClientStatus;
import by.javafe.hometask.entity.ClientEntity;
import by.javafe.hometask.entity.VisitorEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {
    private final EntityManagerFactory entityManagerFactory;

    public ClientRepositoryImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("sportsPU");
    }

    @Override
    public List<ClientEntity> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT c FROM ClientEntity c", ClientEntity.class).getResultList();
        }
    }

    @Override
    public void add(ClientEntity client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(client);
            transaction.commit();
            System.out.println("Клиент добавлен: " + client);
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении клиента: " + e.getMessage());
        }
    }

    @Override
    public void updateStatus(Long id, ClientStatus newStatus) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            VisitorEntity visitor = entityManager.find(VisitorEntity.class, id);
            if (visitor != null) {
                visitor.setStatus(newStatus);
                entityManager.merge(visitor);
                System.out.println("Статус клиента с ID " + id + " изменён на " + newStatus);
            } else {
                System.out.println("Клиент с ID " + id + " не найден.");
            }
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении статуса: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            ClientEntity client = entityManager.find(ClientEntity.class, id);
            if (client != null) {
                entityManager.remove(client);
                System.out.println("Клиент с ID " + id + " удалён.");
            } else {
                System.out.println("Клиент с ID " + id + " не найден.");
            }
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при удалении клиента: " + e.getMessage());
        }
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
