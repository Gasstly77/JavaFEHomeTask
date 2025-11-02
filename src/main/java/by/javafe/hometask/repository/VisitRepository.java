package by.javafe.hometask.repository;

import by.javafe.hometask.entity.VisitEntity;

import java.util.List;

public interface VisitRepository {
    void save(VisitEntity visit);
    List<VisitEntity> findAll();
    VisitEntity findById(Long id);
}

