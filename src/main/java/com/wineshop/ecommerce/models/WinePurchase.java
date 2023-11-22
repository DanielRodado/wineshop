package com.wineshop.ecommerce.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class WinePurchase {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private int amount;

    private Double subTotal;

    @ManyToOne(fetch = FetchType.EAGER)
    private Wine wine;

    @ManyToOne(fetch = FetchType.EAGER)
    private Purchase purchase;

    // Methods

    // Constructors

    public WinePurchase() {
    }

    public WinePurchase(int amount, Double subTotal) {
        this.amount = amount;
        this.subTotal = subTotal;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public Wine getWine() {
        return wine;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void setWine(Wine wine) {
        this.wine = wine;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }



}
