package by.javafe.hometask.repository;

import by.javafe.hometask.entity.RoomEntity;


import java.math.BigDecimal;
import java.util.List;

public interface RoomRepository {
    RoomEntity findById(Long id);
    void save(RoomEntity room);

    void addCopyWithNewIdentifier(Long sourceRoomId, String newIdentifier);

    void updateHourlyRate(Long roomId, BigDecimal newRate);

    List<RoomEntity> findAll();
}
