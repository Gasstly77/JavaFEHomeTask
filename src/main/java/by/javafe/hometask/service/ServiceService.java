package by.javafe.hometask.service;

import by.javafe.hometask.entity.ServiceEntity;
import by.javafe.hometask.repository.ServiceRepositoryImpl;

import java.util.List;

public class ServiceService {
    private final ServiceRepositoryImpl serviceRepository;

    public ServiceService() {
        this.serviceRepository = new ServiceRepositoryImpl();
    }

    public void saveService(ServiceEntity service) {
        serviceRepository.saveService(service);
    }

    public List<ServiceEntity> findAllServices() {
        return serviceRepository.findAllServices();
    }

    public ServiceEntity findCheapestServiceUsingCriteria() {
        return serviceRepository.findCheapestServiceUsingCriteria();
    }

    public ServiceEntity findById(Long id) {
        return serviceRepository.findById(id);
    }

    /**
     * Демонстрация выполнения 3 запросов:
     * 1. Получение всех активностей
     * 2. Получение всех работников
     * 3. Поиск активности по id
     * 
     * @param serviceId ID активности для поиска
     * @param useCache true - использовать одну сессию (кэш 1-го уровня, 2 запроса в БД)
     *                false - использовать разные сессии (3 запроса в БД)
     */
    public void demonstrateQueries(Long serviceId, boolean useCache) {
        System.out.println("\n=== Демонстрация запросов (useCache: " + useCache + ") ===");
        
        if (useCache) {
            demonstrateWithFirstLevelCache(serviceId);
        } else {
            demonstrateWithoutCache(serviceId);
        }
    }

    private void demonstrateWithFirstLevelCache(Long serviceId) {
        System.out.println("\n--- Сценарий с кэшем 1-го уровня (одна сессия) ---");
        System.out.println("Ожидается: 2 запроса в БД");
        System.out.println("1. SELECT всех активностей");
        System.out.println("2. SELECT всех работников");
        System.out.println("3. Поиск активности по id - будет использован кэш 1-го уровня, запроса не будет\n");

        org.hibernate.Session session = by.javafe.hometask.config.HibernateConfig.getSessionFactory().openSession();
        try {
            System.out.println("Запрос 1: Получение всех активностей...");
            List<ServiceEntity> services = session.createQuery("FROM ServiceEntity", ServiceEntity.class).list();
            System.out.println("Найдено активностей: " + services.size());

            System.out.println("\nЗапрос 2: Получение всех работников...");
            List<by.javafe.hometask.entity.EmployeeEntity> employees = 
                    session.createQuery("FROM EmployeeEntity", by.javafe.hometask.entity.EmployeeEntity.class).list();
            System.out.println("Найдено работников: " + employees.size());

            System.out.println("\nЗапрос 3: Поиск активности по id " + serviceId + "...");
            ServiceEntity service = session.get(ServiceEntity.class, serviceId);
            if (service != null) {
                System.out.println("Найдена активность: " + service.getName() + " (из кэша 1-го уровня, запроса в БД не было)");
            } else {
                System.out.println("Активность не найдена");
            }
            
            System.out.println("\n✅ Итого: 2 запроса в БД (третий использовал кэш 1-го уровня)");
        } finally {
            session.close();
        }
    }

    private void demonstrateWithoutCache(Long serviceId) {
        System.out.println("\n--- Сценарий без кэша (разные сессии) ---");
        System.out.println("Ожидается: 3 запроса в БД");
        System.out.println("1. SELECT всех активностей");
        System.out.println("2. SELECT всех работников");
        System.out.println("3. SELECT активности по id\n");

        org.hibernate.Session session1 = by.javafe.hometask.config.HibernateConfig.getSessionFactory().openSession();
        try {
            System.out.println("Запрос 1: Получение всех активностей...");
            List<ServiceEntity> services = session1.createQuery("FROM ServiceEntity", ServiceEntity.class).list();
            System.out.println("Найдено активностей: " + services.size());
        } finally {
            session1.close();
        }
        
        org.hibernate.Session session2 = by.javafe.hometask.config.HibernateConfig.getSessionFactory().openSession();
        try {
            System.out.println("\nЗапрос 2: Получение всех работников...");
            List<by.javafe.hometask.entity.EmployeeEntity> employees = 
                    session2.createQuery("FROM EmployeeEntity", by.javafe.hometask.entity.EmployeeEntity.class).list();
            System.out.println("Найдено работников: " + employees.size());
        } finally {
            session2.close();
        }
        
        org.hibernate.Session session3 = by.javafe.hometask.config.HibernateConfig.getSessionFactory().openSession();
        try {
            System.out.println("\nЗапрос 3: Поиск активности по id " + serviceId + "...");
            ServiceEntity service = session3.get(ServiceEntity.class, serviceId);
            if (service != null) {
                System.out.println("Найдена активность: " + service.getName() + " (запрос выполнен в БД)");
            } else {
                System.out.println("Активность не найдена");
            }
        } finally {
            session3.close();
        }
        
        System.out.println("\n✅ Итого: 3 запроса в БД");
    }
}
