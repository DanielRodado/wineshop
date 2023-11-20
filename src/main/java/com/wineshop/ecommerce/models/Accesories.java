package com.wineshop.ecommerce.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Accesories {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name, description;

    @ElementCollection
    private List<String> imagesURL;

    private Double price;

    private int stock;

    @OneToMany(mappedBy = "accessories", fetch = FetchType.EAGER)
    private Set<AccessoriesOrder> accessoriesOrders = new HashSet<>();

    // Methods

    // Constructors

    public Accesories() { }

    public Accesories(String name, String description, List<String> imagesURL, Double price, int stock) {
        this.name = name;
        this.description = description;
        this.imagesURL = imagesURL;
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

    public List<String> getImagesURL() {
        return imagesURL;
    }

    public void setImagesURL(List<String> imagesURL) {
        this.imagesURL = imagesURL;
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

    // other methods

    public void addAccessoriesOrder (AccessoriesOrder accessoriesOrder) {
        this.accessoriesOrders.add(accessoriesOrder);
        accessoriesOrder.setAccessories(this);
    }
}
