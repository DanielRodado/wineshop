package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.dto.PurchaseDTO;
import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.repositories.PurchaseRepository;
import com.wineshop.ecommerce.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public Boolean existsPurchaseById(Long id) {
        return purchaseRepository.existsById(id);
    }

    @Override
    public Boolean existsPurchaseByIdAndClient(Long id, Client client) {
        return purchaseRepository.existsByIdAndClient(id, client);
    }

    @Override
    public Set<Purchase> getPurchasesByClient(Client client) {
        return purchaseRepository.getPurchasesByClient(client);
    }

    @Override
    public Set<PurchaseDTO> getPurchaseDTOByClient(Client client) {
        return getPurchasesByClient(client).stream().map(PurchaseDTO::new).collect(Collectors.toSet());
    }
}
