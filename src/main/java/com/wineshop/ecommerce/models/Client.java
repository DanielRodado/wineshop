package com.wineshop.ecommerce.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Client {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String firstName, lastName, password, email;

    private Boolean isAdmin;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Order> orders = new HashSet<>();

    // Methods

    // Constructors

    public Client() { }

    public Client(String firstName, String lastName, String password, String email, Boolean isAdmin, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.birthDate = birthDate;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    // Other methods

    public String fullName(){
        return this.firstName + " " + this.lastName;
    }

    public void addOrder(Order order) {
        order.setClient(this);
        this.orders.add(order);
    }
}
