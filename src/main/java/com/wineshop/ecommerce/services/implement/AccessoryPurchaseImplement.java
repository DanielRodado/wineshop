package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.models.AccessoryPurchase;
import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.repositories.AccessoryPurchaseRepository;
import com.wineshop.ecommerce.services.AccessoryPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessoryPurchaseImplement implements AccessoryPurchaseService {

    @Autowired
    private AccessoryPurchaseRepository accessoryPurchaseRepository;

    @Override
    public void saveAccessoryPurchase(AccessoryPurchase accessoryPurchase) {
        accessoryPurchaseRepository.save(accessoryPurchase);
    }

    @Override
    public void deleteAccessoryPurchasesByPurchase(Purchase purchase) {
        accessoryPurchaseRepository.deleteAccessoryPurchasesByPurchase(purchase);
    }
}
