package by.javafe.hometask;

import by.javafe.hometask.constant.RoomStatus;
import by.javafe.hometask.entity.*;
import by.javafe.hometask.service.RoomService;
import by.javafe.hometask.service.ServiceService;
import by.javafe.hometask.service.VisitorService;
import by.javafe.hometask.service.EmployeeService;
import by.javafe.hometask.constant.ClientStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        visitorService.close();
        employeeService.close();
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
