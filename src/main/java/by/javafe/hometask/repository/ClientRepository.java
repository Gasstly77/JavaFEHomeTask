package by.javafe.hometask.repository;

import by.javafe.hometask.constant.ClientStatus;
import by.javafe.hometask.entity.ClientEntity;

import java.util.List;

public interface ClientRepository {
    List<ClientEntity> getAll();

    void add(ClientEntity client);

    void updateStatus(Long id, ClientStatus newStatus);

    void delete(Long id);

    List<ClientEntity> findByName(String name);
}
