package com.wineshop.ecommerce.controllers;

import com.wineshop.ecommerce.dto.ClientDTO;
import com.wineshop.ecommerce.dto.NewClientApplicationDTO;
import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.wineshop.ecommerce.utils.ClientUtil.isValidEmail;
import static com.wineshop.ecommerce.utils.ClientUtil.isValidPassword;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getAllClientsDTO() {
        return clientService.getAllClientsDTO();
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO( clientService.findClientByEmail(authentication.getName()) );
    }

    @GetMapping("/clients/online")
    public ResponseEntity<Object> clientOnline(Authentication authentication) {
        return new ResponseEntity<>(authentication != null ? HttpStatus.OK : HttpStatus.FORBIDDEN);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> registerNewClient (
            @RequestBody NewClientApplicationDTO newClientApp) {

        // Revisar que los campos no esten vacios

        if (newClientApp.getFirstName().isBlank()) {
            return new ResponseEntity<>("Your first name is missing", HttpStatus.FORBIDDEN);
        }

        if (newClientApp.getLastName().isBlank()) {
            return new ResponseEntity<>("Your last name is missing", HttpStatus.FORBIDDEN);
        }

        // ver que el email tenga el formato correcto

        if (!isValidEmail(newClientApp.getEmail())) {
            return new ResponseEntity<>("Please enter a valid email", HttpStatus.FORBIDDEN);
        }

        // ver que la contraseña cumpla con cierta condicion (expresiones regulares)

        if (!isValidPassword(newClientApp.getPassword())) {
            return new ResponseEntity<>("Your password doesn't match the requirements", HttpStatus.FORBIDDEN);
        }

        // ver que la fecha no este vacia

        if (newClientApp.getBirthDate() == null || newClientApp.getBirthDate().equals(LocalDate.MIN)) {
            return new ResponseEntity<>("Birth date needs to be filled in", HttpStatus.FORBIDDEN);
        }

        // ver que el cliente sea mayor de 18 años

        if (newClientApp.getBirthDate().plusYears(18).isAfter(LocalDate.now())) {
            return new ResponseEntity<>("You have to be 18 or older to register", HttpStatus.FORBIDDEN);
        }

        // ver que el email no exista en la base de datos

        if (clientService.existsClientByEmail(newClientApp.getEmail())) {
            return new ResponseEntity<>("This email is already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(newClientApp.getFirstName(), newClientApp.getLastName(),
                passwordEncoder.encode(newClientApp.getPassword()), newClientApp.getEmail(),
                false,newClientApp.getBirthDate());

        clientService.saveClient(client);

        return new ResponseEntity<>("Register successful", HttpStatus.CREATED);
    }


}
