package by.javafe.hometask.repository;

import by.javafe.hometask.config.HibernateUtil;
import by.javafe.hometask.entity.RoomEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository{
    @Override
    public RoomEntity findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RoomEntity.class, id);
        }
    }
    @Override
    public void save(RoomEntity room) {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            RoomEntity sourceRoom = session.get(RoomEntity.class, sourceRoomId);
            if (sourceRoom == null) {
                throw new RuntimeException("Исходное помещение не найдено: id=" + sourceRoomId);
            }

            session.detach(sourceRoom);

            RoomEntity newRoom = new RoomEntity();
            newRoom.setName(sourceRoom.getName());
            newRoom.setIdentifier(newIdentifier);
            newRoom.setMaxCapacity(sourceRoom.getMaxCapacity());
            newRoom.setStatus(sourceRoom.getStatus());
            newRoom.setHourlyRate(sourceRoom.getHourlyRate());

            session.persist(newRoom);
            transaction.commit();

            System.out.println("✅ Создана копия помещения: " + newRoom);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при копировании помещения", e);
        }
    }
    @Override
    public void updateHourlyRate(Long roomId, BigDecimal newRate) {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM RoomEntity", RoomEntity.class).list();
        }
    }
}
