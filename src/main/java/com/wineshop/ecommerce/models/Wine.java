package com.wineshop.ecommerce.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Wine {

    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name, description, area, vineyard;

    private Double price;

    @ElementCollection
    private List<String> imgURL;

    private int stock, cc;

    private Variety variety;

    @OneToMany(mappedBy = "wine", fetch = FetchType.EAGER)
    private Set<WineOrder> wineOrders = new HashSet<>();


    // Constructors

    public Wine() {
    }

    public Wine(String name, String description, String area, String vineyard, Double price, List<String> imgURL, int stock, int cc, Variety variety) {
        this.name = name;
        this.description = description;
        this.area = area;
        this.vineyard = vineyard;
        this.price = price;
        this.imgURL = imgURL;
        this.stock = stock;
        this.cc = cc;
        this.variety = variety;
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

    public String getArea() {
        return area;
    }

    public String getVineyard() {
        return vineyard;
    }

    public Double getPrice() {
        return price;
    }

    public List<String> getImgURL() {
        return imgURL;
    }

    public int getStock() {
        return stock;
    }

    public int getCc() {
        return cc;
    }

    public Variety getVariety() {
        return variety;
    }


    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setVineyard(String vineyard) {
        this.vineyard = vineyard;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImgURL(List<String> imgURL) {
        this.imgURL = imgURL;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    // Methods
    public void addWineOrder(WineOrder wineOrder) {
        wineOrders.add(wineOrder);
        wineOrder.setWine(this);
    }
}
