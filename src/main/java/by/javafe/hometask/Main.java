package by.javafe.hometask;

import by.javafe.hometask.constant.ClientStatus;
import by.javafe.hometask.entity.ClientEntity;
import by.javafe.hometask.service.ClientService;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = new ClientService();

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

    private static ClientEntity buildClient (String firstName, String lastName, Integer age, String phoneNumber, LocalDate lastVisitDate, ClientStatus status, Double totalSpent) {
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