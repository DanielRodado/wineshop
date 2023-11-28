package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.models.AccessoryPurchase;
import com.wineshop.ecommerce.models.Purchase;

public interface AccessoryPurchaseService {

    void saveAccessoryPurchase(AccessoryPurchase accessoryPurchase);

    void deleteAccessoryPurchasesByPurchase(Purchase purchase);

}
