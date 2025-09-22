package by.javafe.hometask.service;

import by.javafe.hometask.constant.ClientStatus;
import by.javafe.hometask.entity.ClientEntity;
import jakarta.persistence.*;

import java.util.List;

public class ClientService {

    private final EntityManagerFactory entityManagerFactory;

    public ClientService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("sportsPU");
    }

    // Добавить клиента
    public void addClient(ClientEntity client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(client);
            transaction.commit();
            System.out.println("✅ Клиент добавлен: " + client);
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении клиента: " + e.getMessage());
        }
    }

    // Получить всех клиентов
    public List<ClientEntity> getAllClients() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT c FROM ClientEntity c", ClientEntity.class).getResultList();
        }
    }

    // Удалить клиента по ID
    public void deleteClient(Long id) {
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

    // Изменить статус клиента по ID
    public void updateClientStatus(Long id, ClientStatus newStatus) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            ClientEntity client = entityManager.find(ClientEntity.class, id);
            if (client != null) {
                client.setStatus(newStatus);
                entityManager.merge(client);
                System.out.println("Статус клиента с ID " + id + " изменён на " + newStatus);
            } else {
                System.out.println("Клиент с ID " + id + " не найден.");
            }
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении статуса: " + e.getMessage());
        }
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}