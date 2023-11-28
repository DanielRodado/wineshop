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
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    @Override
    public void savePurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    @Override
    public void deletePurchaseById(Long id) {
        purchaseRepository.deleteById(id);
    }

    @Override
    public Long savePurchaseAndGetId(Purchase purchase) {
        Purchase purchaseSave = purchaseRepository.save(purchase);
        return purchaseSave.getId();
    }

    @Override
    public Double getPriceOrderOfPurchaseById(Long id) {
        return purchaseRepository.getPriceOrderById(id);
    }
}
