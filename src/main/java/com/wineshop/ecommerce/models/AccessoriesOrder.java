package com.wineshop.ecommerce.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccessoriesOrder {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private int amount;

    private Double subTotal;

    // propiedad de accesorios y order

    // Constructors

    public AccessoriesOrder() {
    }

    public AccessoriesOrder(int amount, Double subTotal) {
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
}
