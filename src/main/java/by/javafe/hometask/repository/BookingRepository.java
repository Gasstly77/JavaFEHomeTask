package by.javafe.hometask.repository;

import by.javafe.hometask.entity.BookingEntity;

import java.util.List;

public interface BookingRepository {
    void save(BookingEntity booking);
    List<BookingEntity> findAll();
    BookingEntity findById(Long id);
    void delete(Long id);
}

