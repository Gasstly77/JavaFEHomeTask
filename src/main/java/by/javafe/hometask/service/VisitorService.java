package by.javafe.hometask.service;

import by.javafe.hometask.entity.VisitorEntity;
import by.javafe.hometask.repository.VisitorRepositoryImpl;

import java.util.List;

public class VisitorService {
    private final VisitorRepositoryImpl visitorRepository;

    public VisitorService() {
        this.visitorRepository = new VisitorRepositoryImpl();
    }

    public void addVisitor(VisitorEntity visitor) {
        visitorRepository.add(visitor);
    }

    public List<VisitorEntity> getAllVisitors() {
        return visitorRepository.getAll();
    }

    public void deleteVisitor(Long id) {
        visitorRepository.delete(id);
    }

    public void close() {
        visitorRepository.close();
    }
}

