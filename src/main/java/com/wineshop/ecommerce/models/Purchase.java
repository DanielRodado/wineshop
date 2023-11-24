package com.wineshop.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Purchase {

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

    @OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER)
    private Set<WinePurchase> winePurchases = new HashSet<>();

    @OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER)
    private Set<AccessoryPurchase> accessoryPurchases = new HashSet<>();

    // Methods

    // Constructors

    public Purchase() { }

    public Purchase(String deliveryAddress, LocalDate date) {
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

    public Set<WinePurchase> getWinePurchases() {
        return winePurchases;
    }

    public Set<AccessoryPurchase> getAccessoryPurchases() {
        return accessoryPurchases;
    }

    // other methods

    public void addAccessoryPurchases(AccessoryPurchase accessoryPurchase) {
        this.accessoryPurchases.add(accessoryPurchase);
        accessoryPurchase.setPurchase(this);
    }

    public void addWinePurchase(WinePurchase winePurchase) {
        this.winePurchases.add(winePurchase);
        winePurchase.setPurchase(this);
    }
}