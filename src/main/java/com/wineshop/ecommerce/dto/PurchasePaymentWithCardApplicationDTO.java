package com.wineshop.ecommerce.dto;

public class PurchasePaymentWithCardApplicationDTO {

    private NewPurchaseApplicationDTO newPurchaseApp;

    private PayWithCardApplicationDTO payWithCardApp;

    public PurchasePaymentWithCardApplicationDTO() {
    }

    public NewPurchaseApplicationDTO getNewPurchaseApp() {
        return newPurchaseApp;
    }

    public PayWithCardApplicationDTO getPayWithCardApp() {
        return payWithCardApp;
    }
}
