package by.javafe.hometask.repository;

import by.javafe.hometask.config.HibernateUtil;
import by.javafe.hometask.entity.ServiceEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ServiceRepositoryImpl implements ServiceRepository{
    @Override
    public void saveService (ServiceEntity service) {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(service);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения услуги", e);
        }
    }
    @Override
    public List<ServiceEntity> findAllServices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM ServiceEntity", ServiceEntity.class).list();
        }
    }
}
