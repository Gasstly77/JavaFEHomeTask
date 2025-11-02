package by.javafe.hometask.repository;

import by.javafe.hometask.config.HibernateConfig;
import by.javafe.hometask.entity.RoomEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository{
    @Override
    public RoomEntity findById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(RoomEntity.class, id);
        }
    }
    @Override
    public void save(RoomEntity room) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения помещения", e);
        }
    }
    @Override
    public void addCopyWithNewIdentifier(Long sourceRoomId, String newIdentifier) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            RoomEntity sourceRoom = session.get(RoomEntity.class, sourceRoomId);
            if (sourceRoom == null) {
                throw new RuntimeException("Исходное помещение не найдено: id=" + sourceRoomId);
            }

            session.detach(sourceRoom);

            sourceRoom.setId(null);
            sourceRoom.setIdentifier(newIdentifier);

            session.persist(sourceRoom);
            transaction.commit();

            System.out.println("✅ Создана копия помещения: " + sourceRoom);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при копировании помещения", e);
        }
    }
    @Override
    public void updateHourlyRate(Long roomId, BigDecimal newRate) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            RoomEntity room = session.get(RoomEntity.class, roomId);
            if (room == null) {
                throw new RuntimeException("Помещение не найдено: id=" + roomId);
            }

            room.setHourlyRate(newRate);
            transaction.commit();

            System.out.println("✅ Стоимость аренды обновлена: " + room);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении стоимости", e);
        }
    }
    @Override
    public List<RoomEntity> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM RoomEntity", RoomEntity.class).list();
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            RoomEntity room = session.get(RoomEntity.class, id);
            if (room != null) {
                session.remove(room);
                System.out.println("✅ Помещение с ID " + id + " удалено.");
            } else {
                System.out.println("⚠️ Помещение с ID " + id + " не найдено.");
            }
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении помещения", e);
        }
    }

    @Override
    public BigDecimal calculatePricePerPersonPerHour(String roomName) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<RoomEntity> rooms = session.createQuery(
                    "FROM RoomEntity r WHERE LOWER(r.name) LIKE LOWER(:name)",
                    RoomEntity.class)
                    .setParameter("name", "%" + roomName + "%")
                    .list();

            if (rooms.isEmpty()) {
                throw new RuntimeException("Тренажёрный зал с названием, содержащим '" + roomName + "', не найден.");
            }

            // Берем первое найденное помещение
            RoomEntity room = rooms.get(0);

            if (room.getMaxCapacity() == null || room.getMaxCapacity() <= 0) {
                throw new RuntimeException("Некорректная максимальная вместимость для помещения: " + room.getName());
            }

            // Рассчитываем стоимость за час на 1 человека
            double hourlyRate = room.getHourlyRate().doubleValue();
            double capacity = room.getMaxCapacity().doubleValue();
            double pricePerPerson = hourlyRate / capacity;
            
            // Округляем до 2 знаков после запятой
            double rounded = Math.round(pricePerPerson * 100.0) / 100.0;
            return BigDecimal.valueOf(rounded);
        }
    }

    @Override
    public Integer getTotalCapacityUsingCriteria() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("sportsPU");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
            Root<RoomEntity> root = query.from(RoomEntity.class);
            
            // Используем sum для подсчета общей вместимости
            query.select(cb.sum(root.<Integer>get("maxCapacity")));
            
            Integer result = entityManager.createQuery(query).getSingleResult();
            return result != null ? result : 0;
        } finally {
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
        }
    }

    @Override
    public List<RoomEntity> findRoomsVisitedByGuestsOver50UsingCriteria() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("sportsPU");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<RoomEntity> query = cb.createQuery(RoomEntity.class);
            
            // Начинаем с BookingEntity для доступа к visitor и room
            Root<by.javafe.hometask.entity.BookingEntity> bookingRoot = query.from(by.javafe.hometask.entity.BookingEntity.class);
            
            // Join с VisitorEntity
            Join<by.javafe.hometask.entity.BookingEntity, by.javafe.hometask.entity.VisitorEntity> visitorJoin = 
                    bookingRoot.join("visitor", JoinType.INNER);
            
            // Join с RoomEntity
            Join<by.javafe.hometask.entity.BookingEntity, RoomEntity> roomJoin = 
                    bookingRoot.join("room", JoinType.INNER);
            
            // Вычисляем год рождения для возраста 50 лет
            int currentYear = java.time.LocalDate.now().getYear();
            int maxYearOfBirth = currentYear - 50; // Гости старше 50 лет родились до этого года
            
            // Создаем предикат: yearOfBirth < (currentYear - 50)
            // VisitorEntity наследуется от ClientEntity, поэтому используем visitorJoin
            Predicate agePredicate = cb.lessThan(visitorJoin.get("yearOfBirth"), maxYearOfBirth);
            
            // Выбираем уникальные помещения
            query.select(roomJoin).where(agePredicate).distinct(true);
            
            return entityManager.createQuery(query).getResultList();
        } finally {
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
        }
    }
}
