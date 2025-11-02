package by.javafe.hometask.repository;

import by.javafe.hometask.entity.ServiceEntity;

import java.util.List;

public interface ServiceRepository {
    void saveService(ServiceEntity service);

    List<ServiceEntity> findAllServices();

    ServiceEntity findCheapestServiceUsingCriteria();
}
