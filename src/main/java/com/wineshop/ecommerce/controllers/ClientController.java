package com.wineshop.ecommerce.controllers;

import com.wineshop.ecommerce.dto.ClientDTO;
import com.wineshop.ecommerce.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientService clientService;

//    @GetMapping("/clients")
//    public List<ClientDTO> getAllClientsDTO() {
//        return clientService.getAllClientDTO();
//    }

}
