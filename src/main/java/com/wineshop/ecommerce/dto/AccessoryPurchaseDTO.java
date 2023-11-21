package com.wineshop.ecommerce.dto;

import com.wineshop.ecommerce.models.Accessory;
import com.wineshop.ecommerce.models.AccessoryPurchase;
import com.wineshop.ecommerce.models.Purchase;

public class AccessoryPurchaseDTO {

    // Properties

    private Long id;

    private int amount;

    private Double subTotal;

    private Purchase purchase;

    private Accessory accessory;

    // Constructor

    public AccessoryPurchaseDTO (AccessoryPurchase accessoryPurchase) {
        this.id = accessoryPurchase.getId();
        this.amount = accessoryPurchase.getAmount();
        this.subTotal = accessoryPurchase.getSubTotal();
        this.purchase = accessoryPurchase.getPurchase();
        this.accessory = accessoryPurchase.getAccessory();
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

    public Purchase getPurchase() {
        return purchase;
    }

    public Accessory getAccessory() {
        return accessory;
    }
}
