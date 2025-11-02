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
}
