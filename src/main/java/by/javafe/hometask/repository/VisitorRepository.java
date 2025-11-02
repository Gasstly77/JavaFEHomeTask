package by.javafe.hometask.repository;

import by.javafe.hometask.entity.VisitorEntity;

import java.util.List;

public interface VisitorRepository {
    List<VisitorEntity> getAll();

    void add(VisitorEntity visitor);

    void delete(Long id);
}

