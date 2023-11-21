package com.wineshop.ecommerce.dto;

import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.models.Purchase;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class PurchaseDTO {

    // Properties

    private Long id;

    private String deliveryAddress;

    private LocalDate date;

    private Double priceOrder;

    private Set<WinePurchaseDTO> winePurchases;

    private Set<AccessoryPurchaseDTO> accessoryPurchases;

    // Constructor

    public PurchaseDTO (Purchase purchase) {
        this.id = purchase.getId();
        this.deliveryAddress = purchase.getDeliveryAddress();
        this.date = purchase.getDate();
        this.priceOrder = purchase.getPriceOrder();
        this.winePurchases = purchase.getWinePurchases().stream().map(WinePurchaseDTO::new).collect(Collectors.toSet());
        this.accessoryPurchases = purchase.getAccessoryPurchases().stream().map(AccessoryPurchaseDTO::new).collect(Collectors.toSet());
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getPriceOrder() {
        return priceOrder;
    }

    public Set<WinePurchaseDTO> getWinePurchases() {
        return winePurchases;
    }

    public Set<AccessoryPurchaseDTO> getAccessoryPurchases() {
        return accessoryPurchases;
    }
}
