package com.wineshop.ecommerce.dto;

import com.wineshop.ecommerce.models.Accessory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccessoryDTO {

    // Properties

    private Long id;

    private String name, description;

    private List<String> imagesURL;

    private Double price;

    private int stock;

    private Set<AccessoryPurchaseDTO> accessoryPurchases;

    // Constructor

    public AccessoryDTO(Accessory accessory) {
        this.id = accessory.getId();
        this.name = accessory.getName();
        this.description = accessory.getDescription();
        this.imagesURL = accessory.getImgURL();
        this.price = accessory.getPrice();
        this.stock = accessory.getStock();
        this.accessoryPurchases = accessory.getAccessoryPurchases().stream().map(AccessoryPurchaseDTO::new).collect(Collectors.toSet());
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImagesURL() {
        return imagesURL;
    }

    public Double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public Set<AccessoryPurchaseDTO> getAccessoryPurchases() {
        return accessoryPurchases;
    }
}
