package by.javafe.hometask.service;

import by.javafe.hometask.entity.VisitEntity;
import by.javafe.hometask.repository.VisitRepository;
import by.javafe.hometask.repository.VisitRepositoryImpl;

import java.util.List;

public class VisitService {
    private final VisitRepository visitRepository;

    public VisitService() {
        this.visitRepository = new VisitRepositoryImpl();
    }

    public void save(VisitEntity visit) {
        visitRepository.save(visit);
    }

    public List<VisitEntity> findAll() {
        return visitRepository.findAll();
    }

    public VisitEntity findById(Long id) {
        return visitRepository.findById(id);
    }
}

