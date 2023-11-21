package com.wineshop.ecommerce.dto;

import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.models.Wine;
import com.wineshop.ecommerce.models.WinePurchase;

public class WinePurchaseDTO {

    // Properties

    private Long id;

    private int amount;

    private Double subTotal;

    private Long wineId;

    // Constructor

    public WinePurchaseDTO (WinePurchase winePurchase) {
        this.id = winePurchase.getId();
        this.amount = winePurchase.getAmount();
        this.subTotal = winePurchase.getSubTotal();
        this.wineId = winePurchase.getWine().getId();
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

    public Long getWineId() {
        return wineId;
    }
}
