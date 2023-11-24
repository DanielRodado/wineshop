package com.wineshop.ecommerce.dto;

public class WineValuationDTO {

    private String wineName;

    private double valuation;

    public WineValuationDTO(String wineName, double valuation) {
        this.wineName = wineName;
        this.valuation = valuation;
    }

    public String getWineName() {
        return wineName;
    }

    public double getValuation() {
        return valuation;
    }
}
