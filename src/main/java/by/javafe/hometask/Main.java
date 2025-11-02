package by.javafe.hometask;

import by.javafe.hometask.constant.RoomStatus;
import by.javafe.hometask.entity.*;
import by.javafe.hometask.service.RoomService;
import by.javafe.hometask.service.ServiceService;
import by.javafe.hometask.service.VisitorService;
import by.javafe.hometask.service.EmployeeService;
import by.javafe.hometask.service.VisitService;
import by.javafe.hometask.service.BookingService;
import by.javafe.hometask.service.ClientService;
import by.javafe.hometask.constant.ClientStatus;
import by.javafe.hometask.config.HibernateConfig;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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

        roomService.addCopyWithNewIdentifier(9L, "GYM-002");
        roomService.addCopyWithNewIdentifier(9L, "GYM-003");

        roomService.updateHourlyRate(9L, BigDecimal.valueOf(600));

        System.out.println("\n=== Все помещения ===");
        List<RoomEntity> rooms = roomService.findAll();
        rooms.forEach(System.out::println);

        VisitorService visitorService = new VisitorService();
        EmployeeService employeeService = new EmployeeService();

        visitorService.addVisitor(buildVisitor("Анна", "Волкова", 1990,
                createAddress("Минск", "Проспект Победителей", "10", "220000"),
                ClientStatus.ACTIVE, LocalDate.of(2025, 1, 15), LocalDate.of(2025, 1, 25), 2500.0));

        visitorService.addVisitor(buildVisitor("Сергей", "Морозов", 1988,
                createAddress("Минск", "Улица Ленина", "25", "220001"),
                ClientStatus.PREMIUM, LocalDate.of(2025, 1, 10), LocalDate.of(2025, 1, 26), 5500.0));

        visitorService.addVisitor(buildVisitor("Елена", "Котова", 1992,
                createAddress("Минск", "Проспект Независимости", "50", "220002"),
                ClientStatus.ACTIVE, LocalDate.of(2025, 1, 5), LocalDate.of(2025, 1, 24), 1800.0));

        visitorService.addVisitor(buildVisitor("Павел", "Орлов", 1985,
                createAddress("Минск", "Улица Советская", "8", "220003"),
                ClientStatus.PREMIUM, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 27), 7500.0));

        System.out.println("\n=== Все посетители ===");
        List<VisitorEntity> visitors = visitorService.getAllVisitors();
        for (VisitorEntity visitor : visitors) {
            System.out.println(visitor);
        }

        employeeService.addEmployee(buildEmployee("Александр", "Новиков", 1980,
                createAddress("Минск", "Проспект Победителей", "15", "220000"),
                LocalDate.of(2020, 3, 1), null, "Тренер", BigDecimal.valueOf(2500)));

        employeeService.addEmployee(buildEmployee("Марина", "Зайцева", 1990,
                createAddress("Минск", "Улица Ленина", "30", "220001"),
                LocalDate.of(2021, 6, 15), null, "Администратор", BigDecimal.valueOf(1800)));

        employeeService.addEmployee(buildEmployee("Виктор", "Соколов", 1985,
                createAddress("Минск", "Проспект Независимости", "60", "220002"),
                LocalDate.of(2019, 1, 10), LocalDate.of(2024, 12, 31), "Менеджер", BigDecimal.valueOf(2200)));

        employeeService.addEmployee(buildEmployee("Татьяна", "Лебедева", 1995,
                createAddress("Минск", "Улица Советская", "12", "220003"),
                LocalDate.of(2022, 9, 1), null, "Охранник", BigDecimal.valueOf(1500)));

        System.out.println("\n=== Все работники ===");
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        for (EmployeeEntity employee : employees) {
            System.out.println(employee);
        }

        // 1. Связываем услуги с помещениями
        System.out.println("\n=== Связываем услуги с помещениями ===");
        linkServicesToRooms(serviceService, roomService);

        // 2. Создаем посещения для посетителей
        System.out.println("\n=== Создаем посещения ===");
        VisitService visitService = new VisitService();
        createVisits(visitorService, visitService);

        // 3. Создаем записи
        System.out.println("\n=== Создаем записи ===");
        BookingService bookingService = new BookingService();
        createBookings(visitorService, roomService, bookingService);

        // 4. Демонстрируем каскадное удаление
        System.out.println("\n=== Демонстрация каскадного удаления ===");
        demonstrateCascadeDeletion(roomService, bookingService);

        // 5. Демонстрируем новые методы поиска и расчета
        System.out.println("\n=== Демонстрация новых методов ===");
        demonstrateNewMethods(serviceService, visitorService, employeeService, roomService);

        // 6. Демонстрация запросов с кэшем и без
        System.out.println("\n=== Демонстрация работы с кэшем ===");
        Long firstServiceId = services.isEmpty() ? 1L : services.get(0).getId();
        
        // Сценарий а) - 3 запроса в БД (разные сессии, без кэша)
        serviceService.demonstrateQueries(firstServiceId, false);
        
        // Сценарий б) - 2 запроса в БД (одна сессия, кэш 1-го уровня)
        serviceService.demonstrateQueries(firstServiceId, true);

        visitorService.close();
        employeeService.close();
    }

    private static void linkServicesToRooms(ServiceService serviceService, RoomService roomService) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            List<ServiceEntity> services = serviceService.findAllServices();
            List<RoomEntity> rooms = roomService.findAll();

            // Связываем первую услугу (Теннис) с первыми двумя помещениями
            if (!services.isEmpty() && rooms.size() >= 2) {
                ServiceEntity tennisService = services.get(0); // Теннис
                RoomEntity room1 = session.get(RoomEntity.class, rooms.get(0).getId());
                RoomEntity room2 = session.get(RoomEntity.class, rooms.get(1).getId());

                if (room1 != null && tennisService != null) {
                    room1.setService(tennisService);
                    session.merge(room1);
                    System.out.println("✅ Помещение '" + room1.getName() + "' связано с услугой '" + tennisService.getName() + "'");
                }

                if (room2 != null && services.size() > 1) {
                    ServiceEntity swimmingService = services.get(1); // Плавание
                    room2.setService(swimmingService);
                    session.merge(room2);
                    System.out.println("✅ Помещение '" + room2.getName() + "' связано с услугой '" + swimmingService.getName() + "'");
                }
            }

            transaction.commit();
        }
    }

    private static void createVisits(VisitorService visitorService, VisitService visitService) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            List<VisitorEntity> visitors = visitorService.getAllVisitors();

            if (!visitors.isEmpty()) {
                // Создаем посещения для первого посетителя
                VisitorEntity visitor1 = session.get(VisitorEntity.class, visitors.get(0).getId());
                if (visitor1 != null) {
                    VisitEntity visit1 = VisitEntity.builder()
                            .visitDate(LocalDate.of(2025, 1, 15))
                            .amountSpent(BigDecimal.valueOf(1500))
                            .visitor(visitor1)
                            .build();
                    session.persist(visit1);
                    System.out.println("✅ Создано посещение: " + visit1.getVisitDate() + ", сумма: " + visit1.getAmountSpent());

                    VisitEntity visit2 = VisitEntity.builder()
                            .visitDate(LocalDate.of(2025, 1, 20))
                            .amountSpent(BigDecimal.valueOf(2000))
                            .visitor(visitor1)
                            .build();
                    session.persist(visit2);
                    System.out.println("✅ Создано посещение: " + visit2.getVisitDate() + ", сумма: " + visit2.getAmountSpent());
                }

                // Создаем посещение для второго посетителя
                if (visitors.size() > 1) {
                    VisitorEntity visitor2 = session.get(VisitorEntity.class, visitors.get(1).getId());
                    if (visitor2 != null) {
                        VisitEntity visit3 = VisitEntity.builder()
                                .visitDate(LocalDate.of(2025, 1, 18))
                                .amountSpent(BigDecimal.valueOf(3000))
                                .visitor(visitor2)
                                .build();
                        session.persist(visit3);
                        System.out.println("✅ Создано посещение: " + visit3.getVisitDate() + ", сумма: " + visit3.getAmountSpent());
                    }
                }
            }

            transaction.commit();
        }
    }

    private static void createBookings(VisitorService visitorService, RoomService roomService, BookingService bookingService) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            List<VisitorEntity> visitors = visitorService.getAllVisitors();
            List<RoomEntity> rooms = roomService.findAll();

            if (!visitors.isEmpty() && !rooms.isEmpty()) {
                VisitorEntity visitor1 = session.get(VisitorEntity.class, visitors.get(0).getId());
                RoomEntity room1 = session.get(RoomEntity.class, rooms.get(0).getId());

                if (visitor1 != null && room1 != null) {
                    BookingEntity booking1 = BookingEntity.builder()
                            .visitor(visitor1)
                            .room(room1)
                            .bookingDate(LocalDate.of(2025, 2, 1))
                            .bookingTime(LocalTime.of(10, 0))
                            .build();
                    session.persist(booking1);
                    System.out.println("✅ Создана запись: " + booking1.getBookingDate() + " " + booking1.getBookingTime() +
                            " для помещения '" + room1.getName() + "'");

                    BookingEntity booking2 = BookingEntity.builder()
                            .visitor(visitor1)
                            .room(room1)
                            .bookingDate(LocalDate.of(2025, 2, 5))
                            .bookingTime(LocalTime.of(14, 30))
                            .build();
                    session.persist(booking2);
                    System.out.println("✅ Создана запись: " + booking2.getBookingDate() + " " + booking2.getBookingTime() +
                            " для помещения '" + room1.getName() + "'");
                }

                // Создаем запись для другого посетителя и другого помещения
                if (visitors.size() > 1 && rooms.size() > 1) {
                    VisitorEntity visitor2 = session.get(VisitorEntity.class, visitors.get(1).getId());
                    RoomEntity room2 = session.get(RoomEntity.class, rooms.get(1).getId());

                    if (visitor2 != null && room2 != null) {
                        BookingEntity booking3 = BookingEntity.builder()
                                .visitor(visitor2)
                                .room(room2)
                                .bookingDate(LocalDate.of(2025, 2, 3))
                                .bookingTime(LocalTime.of(16, 0))
                                .build();
                        session.persist(booking3);
                        System.out.println("✅ Создана запись: " + booking3.getBookingDate() + " " + booking3.getBookingTime() +
                                " для помещения '" + room2.getName() + "'");
                    }
                }
            }

            transaction.commit();
        }
    }

    private static void demonstrateCascadeDeletion(RoomService roomService, BookingService bookingService) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Показываем записи до удаления
            List<BookingEntity> bookingsBefore = bookingService.findAll();
            System.out.println("📋 Записей до удаления помещения: " + bookingsBefore.size());
            bookingsBefore.forEach(b -> System.out.println("  - Запись ID: " + b.getId() +
                    ", помещение: " + (b.getRoom() != null ? b.getRoom().getName() : "N/A") +
                    ", дата: " + b.getBookingDate()));

            // Находим первое помещение
            List<RoomEntity> rooms = roomService.findAll();
            if (!rooms.isEmpty()) {
                RoomEntity roomToDelete = rooms.get(0);
                Long roomId = roomToDelete.getId();

                System.out.println("\n🗑️ Удаляем помещение ID: " + roomId + " (" + roomToDelete.getName() + ")");

                // Удаляем помещение (должны каскадно удалиться все записи)
                roomService.delete(roomId);

                // Показываем записи после удаления
                List<BookingEntity> bookingsAfter = bookingService.findAll();
                System.out.println("\n📋 Записей после удаления помещения: " + bookingsAfter.size());

                // Подсчитываем, сколько записей было удалено
                long deletedCount = bookingsBefore.size() - bookingsAfter.size();
                System.out.println("✅ Каскадно удалено записей: " + deletedCount);
            }
        }
    }

    private static void demonstrateNewMethods(ServiceService serviceService, VisitorService visitorService,
                                             EmployeeService employeeService, RoomService roomService) {
        // 1. Поиск клиента по имени
        System.out.println("\n--- 1. Поиск клиента по имени ---");
        ClientService clientService = new ClientService();
        List<ClientEntity> clientsByName = clientService.findClientByName("Анна");
        if (!clientsByName.isEmpty()) {
            System.out.println("Найдено клиентов: " + clientsByName.size());
            clientsByName.forEach(c -> System.out.println("  - " + c.getFirstName() + " " + c.getLastName()));
        } else {
            System.out.println("Клиенты с именем 'Анна' не найдены.");
        }

        // 2. Самый высокооплачиваемый сотрудник
        System.out.println("\n--- 2. Самый высокооплачиваемый сотрудник ---");
        EmployeeEntity highestPaid = employeeService.findHighestPaidEmployee();
        if (highestPaid != null) {
            System.out.println("ФИО: " + highestPaid.getFirstName() + " " + highestPaid.getLastName());
            System.out.println("Должность: " + highestPaid.getPosition());
            System.out.println("Зарплата: " + highestPaid.getMonthlySalary() + " руб./мес");
        } else {
            System.out.println("Сотрудники не найдены.");
        }

        // 3. Сотрудник с самой низкой зарплатой
        System.out.println("\n--- 3. Сотрудник с самой низкой зарплатой ---");
        EmployeeEntity lowestPaid = employeeService.findLowestPaidEmployee();
        if (lowestPaid != null) {
            System.out.println("ФИО: " + lowestPaid.getFirstName() + " " + lowestPaid.getLastName());
            System.out.println("Должность: " + lowestPaid.getPosition());
            System.out.println("Зарплата: " + lowestPaid.getMonthlySalary() + " руб./мес");
        } else {
            System.out.println("Сотрудники не найдены.");
        }

        // 4. Подсчет расходов на персонал за период
        System.out.println("\n--- 4. Расходы на персонал за период ---");
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        BigDecimal totalExpenses = employeeService.calculateTotalSalaryExpenses(startDate, endDate);
        System.out.println("Период: " + startDate + " - " + endDate);
        System.out.println("Общие расходы на персонал: " + totalExpenses + " руб.");

        // 5. Расчет стоимости за час на 1 человека для тренажёрных залов
        System.out.println("\n--- 5. Стоимость за час на 1 человека (тренажёрные залы) ---");
        try {
            BigDecimal pricePerPerson = roomService.calculatePricePerPersonPerHour("тренажёрный");
            System.out.println("Стоимость за час на 1 человека: " + pricePerPerson + " руб.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        clientService.close();
    }

    private static VisitorEntity buildVisitor(String firstName, String lastName, Integer yearOfBirth,
                                             Address address, ClientStatus status,
                                             LocalDate firstVisitDate, LocalDate lastVisitDate,
                                             Double totalSpent) {
        return VisitorEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .yearOfBirth(yearOfBirth)
                .address(address)
                .status(status)
                .firstVisitDate(firstVisitDate)
                .lastVisitDate(lastVisitDate)
                .totalSpent(totalSpent)
                .build();
    }

    private static EmployeeEntity buildEmployee(String firstName, String lastName, Integer yearOfBirth,
                                                Address address, LocalDate hireDate, LocalDate dismissalDate,
                                                String position, BigDecimal monthlySalary) {
        return EmployeeEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .yearOfBirth(yearOfBirth)
                .address(address)
                .hireDate(hireDate)
                .dismissalDate(dismissalDate)
                .position(position)
                .monthlySalary(monthlySalary)
                .build();
    }

    private static Address createAddress(String city, String street, String houseNumber, String postalCode) {
        Address address = new Address();
        address.setCity(city);
        address.setStreet(street);
        address.setHouseNumber(houseNumber);
        address.setPostalCode(postalCode);
        return address;
    }
}
