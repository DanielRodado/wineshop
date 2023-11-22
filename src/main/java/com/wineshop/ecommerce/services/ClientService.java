package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.dto.ClientDTO;
import com.wineshop.ecommerce.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getAllClientsDTO();

    Client findClientById(Long id);

    boolean existsClientByEmail(String email);

    void saveClient(Client client);
}
