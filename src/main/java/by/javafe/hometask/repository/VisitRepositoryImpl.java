package by.javafe.hometask.repository;

import by.javafe.hometask.config.HibernateConfig;
import by.javafe.hometask.entity.VisitEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VisitRepositoryImpl implements VisitRepository {
    @Override
    public void save(VisitEntity visit) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(visit);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения посещения", e);
        }
    }

    @Override
    public List<VisitEntity> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM VisitEntity", VisitEntity.class).list();
        }
    }

    @Override
    public VisitEntity findById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(VisitEntity.class, id);
        }
    }
}

