package com.wineshop.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Order {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String deliveryAddress;

    private LocalDate date;

    private Double priceOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<WineOrder> wineOrders = new HashSet<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<AccessorieOrder> accessorieOrders = new HashSet<>();

    // Methods

    // Constructors

    public Order() { }

    public Order(String deliveryAddress, LocalDate date, Long client) {
        this.deliveryAddress = deliveryAddress;
        this.date = date;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Double getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(Double priceOrder) {
        this.priceOrder = priceOrder;
    }

    // other methods

    public void addAccessoriesOrder (AccessorieOrder accessorieOrder) {
        this.accessorieOrders.add(accessorieOrder);
        accessorieOrder.setOrder(this);
    }

    public void addWineOrder (WineOrder wineOrder) {
        this.wineOrders.add(wineOrder);
        wineOrder.setOrder(this);
    }
}