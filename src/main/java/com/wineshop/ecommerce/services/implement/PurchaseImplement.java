package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.repositories.PurchaseRepository;
import com.wineshop.ecommerce.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseImplement implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public void savePurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }
}
