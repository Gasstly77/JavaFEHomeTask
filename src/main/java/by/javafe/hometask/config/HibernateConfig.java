package by.javafe.hometask.config;

import by.javafe.hometask.entity.BookingEntity;
import by.javafe.hometask.entity.ClientEntity;
import by.javafe.hometask.entity.RoomEntity;
import by.javafe.hometask.entity.ServiceEntity;
import by.javafe.hometask.entity.VisitEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateConfig {

    private final static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres?currentSchema=sports");
            configuration.setProperty("hibernate.connection.username", "admin");
            configuration.setProperty("hibernate.connection.password", "12345678");

            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            
            // Настройка кэша 2-го уровня через Ehcache (конфигурация в ehcache.xml)
            configuration.setProperty("hibernate.cache.use_second_level_cache", "true");
            configuration.setProperty("hibernate.cache.use_query_cache", "true");
            configuration.setProperty("hibernate.cache.region.factory_class", 
                    "org.hibernate.cache.jcache.JCacheRegionFactory");
            configuration.setProperty("hibernate.javax.cache.provider", 
                    "org.ehcache.jsr107.EhcacheCachingProvider");
            // Указываем путь к XML-файлу конфигурации Ehcache
            configuration.setProperty("hibernate.javax.cache.uri", 
                    "classpath:ehcache.xml");

            configuration.addAnnotatedClass(ClientEntity.class);
            configuration.addAnnotatedClass(ServiceEntity.class);
            configuration.addAnnotatedClass(RoomEntity.class);
            configuration.addAnnotatedClass(VisitEntity.class);
            configuration.addAnnotatedClass(BookingEntity.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
