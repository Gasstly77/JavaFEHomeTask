package by.javafe.hometask.service;

import by.javafe.hometask.entity.RoomEntity;
import by.javafe.hometask.repository.RoomRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;

public class RoomService {
    private final RoomRepositoryImpl roomRepository;

    public RoomService() {
        this.roomRepository = new RoomRepositoryImpl();
    }

    public RoomEntity findById(Long id) {
        return roomRepository.findById(id);
    }

    public void save(RoomEntity room) {
        roomRepository.save(room);
    }

    public void addCopyWithNewIdentifier(Long sourceRoomId, String newIdentifier) {
        roomRepository.addCopyWithNewIdentifier(sourceRoomId, newIdentifier);
    }

    public void updateHourlyRate(Long roomId, BigDecimal newRate) {
        roomRepository.updateHourlyRate(roomId, newRate);
    }

    public List<RoomEntity> findAll() {
        return roomRepository.findAll();
    }
}
