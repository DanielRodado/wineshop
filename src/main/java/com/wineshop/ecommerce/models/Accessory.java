package com.wineshop.ecommerce.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Accessory {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name, description;

    @ElementCollection
    private List<String> imgURL;

    private Double price;

    private int stock;

    @OneToMany(mappedBy = "accessory", fetch = FetchType.EAGER)
    private Set<AccessoryPurchase> accessoryPurchases = new HashSet<>();

    // Methods

    // Constructors

    public Accessory() { }

    public Accessory(String name, String description, List<String> imagesURL, Double price, int stock) {
        this.name = name;
        this.description = description;
        this.imgURL = imagesURL;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImgURL() {
        return imgURL;
    }

    public void setImgURL(List<String> imgURL) {
        this.imgURL = imgURL;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Set<AccessoryPurchase> getAccessoryPurchases() {
        return accessoryPurchases;
    }

    // other methods

    public void addAccessoryPurchase(AccessoryPurchase accessoryPurchase) {
        this.accessoryPurchases.add(accessoryPurchase);
        accessoryPurchase.setAccessory(this);
    }
}
