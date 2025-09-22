package by.javafe.hometask;

import by.javafe.hometask.constant.RoomStatus;
import by.javafe.hometask.entity.RoomEntity;
import by.javafe.hometask.entity.ServiceEntity;
import by.javafe.hometask.service.RoomService;
import by.javafe.hometask.service.ServiceService;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceService serviceService = new ServiceService();
        serviceService.saveService(ServiceEntity.builder().name("Теннис").price(1500.0).build());
        serviceService.saveService(ServiceEntity.builder().name("Плавание").price(1000.0).build());
        serviceService.saveService(ServiceEntity.builder().name("Футбол").price(2000.0).build());
        serviceService.saveService(ServiceEntity.builder().name("Баскетбол").price(1800.0).build());

        System.out.println("\n=== Все услуги ===");
        List<ServiceEntity> services = serviceService.findAllServices();
        services.forEach(System.out::println);

        RoomService roomService = new RoomService();
        roomService.save(RoomEntity.builder()
                .name("Тренажёрный зал")
                .identifier("GYM-001")
                .maxCapacity(20)
                .status(RoomStatus.ACTIVE)
                .hourlyRate(BigDecimal.valueOf(500))
                .build());

        roomService.save(RoomEntity.builder()
                .name("Бассейн")
                .identifier("POOL-001")
                .maxCapacity(50)
                .status(RoomStatus.ACTIVE)
                .hourlyRate(BigDecimal.valueOf(1500))
                .build());

        roomService.addCopyWithNewIdentifier(1L, "GYM-002");
        roomService.addCopyWithNewIdentifier(1L, "GYM-003");

        roomService.updateHourlyRate(1L, BigDecimal.valueOf(600));

        System.out.println("\n=== Все помещения ===");
        List<RoomEntity> rooms = roomService.findAll();
        rooms.forEach(System.out::println);

    }
}