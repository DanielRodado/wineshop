package com.wineshop.ecommerce.dto;

import com.wineshop.ecommerce.models.Client;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    // Properties

    private Long id;

    private String firstName, lastName, email;

    private LocalDate birthDate;

    private Set<PurchaseDTO> purchases;

    private Boolean isAdmin;

    // Constructor

    public ClientDTO (Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.birthDate = client.getBirthDate();
        this.purchases = client.getPurchases().stream().map(PurchaseDTO::new).collect(Collectors.toSet());
        this.isAdmin = client.getAdmin();
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Set<PurchaseDTO> getPurchases() {
        return purchases;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }
}
