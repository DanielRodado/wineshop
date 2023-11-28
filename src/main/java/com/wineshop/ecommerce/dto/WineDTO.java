package com.wineshop.ecommerce.dto;

import com.wineshop.ecommerce.models.Variety;
import com.wineshop.ecommerce.models.Wine;
import com.wineshop.ecommerce.models.WinePurchase;
import com.wineshop.ecommerce.models.WineType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WineDTO {

    // Properties

    private Long id;

    private String name, description, area, vineyard;

    private Double price;

    private List<String> imgURL;

    private int stock, cc;

    private Variety variety;

    private WineType wineType;

    private int valuations, numberOfValuations;

    private Set<WinePurchaseDTO> winePurchases;

    private Boolean isOnOffer;

    // Constructor

    public WineDTO (Wine wine) {
        this.id = wine.getId();
        this.name = wine.getName();
        this.description = wine.getDescription();
        this.area = wine.getArea();
        this.vineyard = wine.getVineyard();
        this.price = wine.getPrice();
        this.imgURL = wine.getImgURL();
        this.stock = wine.getStock();
        this.cc = wine.getCc();
        this.variety = wine.getVariety();
        this.wineType = wine.getWineType();
        this.valuations = wine.getValuations().isEmpty() ?
                0
                :
                (wine.getValuations().stream().reduce((byte) 0, (acc, num) -> (byte) (acc + num)))/wine.getValuations().size();
        this.numberOfValuations = wine.getValuations().size();
        this.winePurchases = wine.getWinePurchases().stream().map(WinePurchaseDTO::new).collect(Collectors.toSet());
        this.isOnOffer = wine.isOnOffer();
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

    public WineType getWineType() {
        return wineType;
    }

    public int getValuations() {
        return valuations;
    }

    public Set<WinePurchaseDTO> getWinePurchases() {
        return winePurchases;
    }

    public int getNumberOfValuations() {
        return numberOfValuations;
    }

    public Boolean getOnOffer() {
        return isOnOffer;
    }
}