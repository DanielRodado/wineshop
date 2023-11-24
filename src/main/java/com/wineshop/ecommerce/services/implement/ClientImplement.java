package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.dto.ClientDTO;
import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.repositories.ClientRepository;
import com.wineshop.ecommerce.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getAllClientsDTO() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public Client findClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsClientByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
