package by.javafe.hometask.service;

import by.javafe.hometask.constant.ClientStatus;
import by.javafe.hometask.entity.ClientEntity;
import by.javafe.hometask.repository.ClientRepositoryImpl;

import java.util.List;

public class ClientService {
    private final ClientRepositoryImpl clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepositoryImpl();
    }

    // Добавить клиента
    public void addClient(ClientEntity client) {
        clientRepository.add(client);
    }

    // Получить всех клиентов
    public List<ClientEntity> getAllClients() {
        return clientRepository.getAll();
    }

    // Удалить клиента по ID
    public void deleteClient(Long id) {
        clientRepository.delete(id);
    }

    // Изменить статус клиента по ID
    public void updateClientStatus(Long id, ClientStatus newStatus) {
        clientRepository.updateStatus(id, newStatus);
    }

    public void close() {
        clientRepository.close();
    }
}