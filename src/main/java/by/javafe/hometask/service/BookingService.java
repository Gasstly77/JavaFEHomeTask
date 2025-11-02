package by.javafe.hometask.service;

import by.javafe.hometask.entity.BookingEntity;
import by.javafe.hometask.repository.BookingRepository;
import by.javafe.hometask.repository.BookingRepositoryImpl;

import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService() {
        this.bookingRepository = new BookingRepositoryImpl();
    }

    public void save(BookingEntity booking) {
        bookingRepository.save(booking);
    }

    public List<BookingEntity> findAll() {
        return bookingRepository.findAll();
    }

    public BookingEntity findById(Long id) {
        return bookingRepository.findById(id);
    }

    public void delete(Long id) {
        bookingRepository.delete(id);
    }
}

