package com.wineshop.ecommerce.dto;

import java.util.HashSet;
import java.util.Set;

public class NewPurchaseApplicationDTO {

    private String deliveryAddress;

    private Set<ProductRecieverDTO> wines = new HashSet<>();
    private Set<ProductRecieverDTO> accessories = new HashSet<>();

    public NewPurchaseApplicationDTO() {
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Set<ProductRecieverDTO> getWines() {
        return wines;
    }

    public Set<ProductRecieverDTO> getAccessories() {
        return accessories;
    }
}
