package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.models.Purchase;

public interface PurchaseService {

    Purchase getPurchaseById(Long id);

    void savePurchase(Purchase purchase);

    void deletePurchaseById(Long id);

    Long savePurchaseAndGetId(Purchase purchase);

    Double getPriceOrderOfPurchaseById(Long id);

    Boolean existsPurchaseById(Long id);

    Boolean existsPurchaseByIdAndClient(Long id, Client client);
}
