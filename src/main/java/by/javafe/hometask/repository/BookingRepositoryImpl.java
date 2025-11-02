package by.javafe.hometask.repository;

import by.javafe.hometask.config.HibernateConfig;
import by.javafe.hometask.entity.BookingEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BookingRepositoryImpl implements BookingRepository {
    @Override
    public void save(BookingEntity booking) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(booking);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения записи", e);
        }
    }

    @Override
    public List<BookingEntity> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM BookingEntity", BookingEntity.class).list();
        }
    }

    @Override
    public BookingEntity findById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(BookingEntity.class, id);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            BookingEntity booking = session.get(BookingEntity.class, id);
            if (booking != null) {
                session.remove(booking);
            }
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления записи", e);
        }
    }
}

