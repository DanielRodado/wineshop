package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.dto.ClientDTO;
import com.wineshop.ecommerce.dto.PurchaseDTO;
import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.models.Purchase;

import java.util.Set;

public interface PurchaseService {

    Purchase getPurchaseById(Long id);

    void savePurchase(Purchase purchase);

    void deletePurchaseById(Long id);

    Long savePurchaseAndGetId(Purchase purchase);

    Double getPriceOrderOfPurchaseById(Long id);

    Boolean existsPurchaseById(Long id);

    Boolean existsPurchaseByIdAndClient(Long id, Client client);

    Set<Purchase> getPurchasesByClient(Client client);

    Set<PurchaseDTO> getPurchaseDTOByClient(Client client);
}
