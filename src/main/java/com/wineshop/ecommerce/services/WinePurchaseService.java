package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.models.WinePurchase;

public interface WinePurchaseService {

    void saveWinePurchase(WinePurchase winePurchase);

    void deleteWinePurchasesByPurchase(Purchase purchase);

}
