package com.wineshop.ecommerce.dto;

import java.time.LocalDate;

public class NewClientApplicationDTO {

    // Properties

    private String firstName, lastName, email, password;

    private LocalDate birthDate;

    // Constructor

    public NewClientApplicationDTO() {
    }

    // Getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
