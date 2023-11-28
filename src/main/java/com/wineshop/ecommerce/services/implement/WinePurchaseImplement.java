package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.models.WinePurchase;
import com.wineshop.ecommerce.repositories.WinePurchaseRepository;
import com.wineshop.ecommerce.services.WinePurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WinePurchaseImplement implements WinePurchaseService {

    @Autowired
    private WinePurchaseRepository winePurchaseRepository;

    @Override
    public void saveWinePurchase(WinePurchase winePurchase) {
        winePurchaseRepository.save(winePurchase);
    }

    @Override
    public void deleteWinePurchasesByPurchase(Purchase purchase) {
        winePurchaseRepository.deleteWinePurchasesByPurchase(purchase);
    }
}
