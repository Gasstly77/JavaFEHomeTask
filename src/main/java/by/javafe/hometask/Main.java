package by.javafe.hometask;

import by.javafe.hometask.constant.RoomStatus;
import by.javafe.hometask.entity.RoomEntity;
import by.javafe.hometask.entity.ServiceEntity;
import by.javafe.hometask.service.RoomService;
import by.javafe.hometask.service.ServiceService;
import by.javafe.hometask.service.ClientService;
import by.javafe.hometask.constant.ClientStatus;
import by.javafe.hometask.entity.ClientEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceService serviceService = new ServiceService();
        ClientService clientService = new ClientService();

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

        clientService.addClient(buildClient("Иван", "Иванов", 25, "+375291112233", LocalDate.of(2025, 9, 20), ClientStatus.ACTIVE, 150.0));
        clientService.addClient(buildClient("Мария", "Петрова", 30, "+375291112244", LocalDate.of(2025, 9, 21), ClientStatus.PREMIUM, 500.0));
        clientService.addClient(buildClient("Алексей", "Сидоров", 22, "+375291112255", LocalDate.of(2025, 9, 15), ClientStatus.BLOCKED, 0.0));
        clientService.addClient(buildClient("Ольга", "Кузнецова", 28, "+375291112266", LocalDate.of(2025, 9, 25), ClientStatus.ACTIVE, 320.0));
        clientService.addClient(buildClient("Дмитрий", "Смирнов", 35, "+375291112277", LocalDate.of(2025, 9, 26), ClientStatus.PREMIUM, 700.0));

        System.out.println("\n=== Все клиенты ===");
        List<ClientEntity> clients = clientService.getAllClients();
        for (ClientEntity client : clients) {
            System.out.println(client);
        }

        clientService.updateClientStatus(1L, ClientStatus.BLOCKED);

        clientService.deleteClient(3L);

        System.out.println("\n=== После изменений ===");
        clients = clientService.getAllClients();
        for (ClientEntity client : clients) {
            System.out.println(client);
        }

        clientService.close();
    }

    private static ClientEntity buildClient(String firstName, String lastName, Integer age, String phoneNumber, LocalDate lastVisitDate, ClientStatus status, Double totalSpent) {
        return ClientEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .phoneNumber(phoneNumber)
                .lastVisitDate(lastVisitDate)
                .status(status)
                .totalSpent(totalSpent)
                .build();
    }
}
