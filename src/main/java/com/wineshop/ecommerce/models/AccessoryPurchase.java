package com.wineshop.ecommerce.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class AccessoryPurchase {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private int amount;

    private Double subTotal;

    @ManyToOne(fetch = FetchType.EAGER)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.EAGER)
    private Accessory accessory;

    // Constructors

    public AccessoryPurchase() {
    }

    public AccessoryPurchase(int amount, Double subTotal) {
        this.amount = amount;
        this.subTotal = subTotal;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    // Setters

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public void setAccessory(Accessory accessory) {
        this.accessory = accessory;
    }
}
