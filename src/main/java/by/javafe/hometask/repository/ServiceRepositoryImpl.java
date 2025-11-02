package by.javafe.hometask.repository;

import by.javafe.hometask.config.HibernateConfig;
import by.javafe.hometask.entity.ServiceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ServiceRepositoryImpl implements ServiceRepository{
    @Override
    public void saveService (ServiceEntity service) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(service);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения услуги", e);
        }
    }
    @Override
    public List<ServiceEntity> findAllServices() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM ServiceEntity", ServiceEntity.class).list();
        }
    }

    @Override
    public ServiceEntity findCheapestServiceUsingCriteria() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("sportsPU");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<ServiceEntity> query = cb.createQuery(ServiceEntity.class);
            Root<ServiceEntity> root = query.from(ServiceEntity.class);
            
            // Сортируем по цене по возрастанию и берем первый результат
            Order orderByPrice = cb.asc(root.get("price"));
            query.select(root).orderBy(orderByPrice);
            
            List<ServiceEntity> result = entityManager.createQuery(query)
                    .setMaxResults(1)
                    .getResultList();
            
            return result.isEmpty() ? null : result.get(0);
        } finally {
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
        }
    }
}
