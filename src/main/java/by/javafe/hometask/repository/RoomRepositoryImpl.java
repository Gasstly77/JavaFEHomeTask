package by.javafe.hometask.repository;

import by.javafe.hometask.config.HibernateConfig;
import by.javafe.hometask.entity.RoomEntity;
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
            // Используем простое деление через double для избежания deprecated методов
            double hourlyRate = room.getHourlyRate().doubleValue();
            double capacity = room.getMaxCapacity().doubleValue();
            double pricePerPerson = hourlyRate / capacity;
            
            // Округляем до 2 знаков после запятой
            // Умножаем на 100, округляем, затем делим на 100
            double rounded = Math.round(pricePerPerson * 100.0) / 100.0;
            return BigDecimal.valueOf(rounded);
        }
    }
}
