package com.wineshop.ecommerce.controllers;

import com.wineshop.ecommerce.dto.NewPurchaseApplicationDTO;
import com.wineshop.ecommerce.dto.ProductRecieverDTO;
import com.wineshop.ecommerce.models.AccessoryPurchase;
import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.models.WinePurchase;
import com.wineshop.ecommerce.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.wineshop.ecommerce.utils.ProductUtil.calculatePriceOrder;

@RestController
@RequestMapping("/api")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private WinePurchaseService winePurchaseService;

    @Autowired
    private AccessoryPurchaseService accessoryPurchaseService;

    @Autowired
    private WineService wineService;

    @Autowired
    private AccessoryService accessoryService;

    @PostMapping("/purchase")
    @Transactional
    public ResponseEntity<String> createPurchase(@RequestBody NewPurchaseApplicationDTO newPurchaseApp) {

        // si las dos listas están vacias, manda un error
        if (newPurchaseApp.getWines().isEmpty() && newPurchaseApp.getAccessories().isEmpty()) {
            return new ResponseEntity<>("Order cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (newPurchaseApp.getDeliveryAddress().isBlank()) {
            return new ResponseEntity<>("The delivery address is missing", HttpStatus.FORBIDDEN);
        }

        Purchase purchase = new Purchase(newPurchaseApp.getDeliveryAddress(), LocalDate.now());
        purchaseService.savePurchase(purchase);

        double priceTotalOrder = 0.0;
        if (!newPurchaseApp.getWines().isEmpty()) {

            for (ProductRecieverDTO wine: newPurchaseApp.getWines()) {
                // Comprobar que el ID de los 'Wines' exista -> Services;

                // Comprobar que la cantidad de Wine a comprar no sea menor o igual a 0;

                // Comprobar si la cantidad a comprar no supere el stock del 'Wine';

                WinePurchase winePurchase = new WinePurchase(
                        wine.getAmount(), calculatePriceOrder(
                                wine.getAmount(), wineService.getPriceWineById(wine.getProductId())));

                priceTotalOrder += calculatePriceOrder(
                        wine.getAmount(), wineService.getPriceWineById(wine.getProductId()));

                // Modificar el stock del 'Wine' -> Hacer las querys en el repositorio.

                purchase.addWinePurchase(winePurchase);
                winePurchaseService.saveWinePurchase(winePurchase);
            }
        }


        if (!newPurchaseApp.getAccessories().isEmpty()) {

            for (ProductRecieverDTO accessories : newPurchaseApp.getAccessories()) {
                // Comprobar que el ID de los 'Wines' exista -> Services;

                // Comprobar que la cantidad de Wine a comprar no sea menor o igual a 0;

                // Comprobar si la cantidad a comprar no supere el stock del 'Wine';

                AccessoryPurchase accessoryPurchase = new AccessoryPurchase(
                        accessories.getAmount(), calculatePriceOrder(
                        accessories.getAmount(), accessoryService.getPriceAccessoryById(accessories.getProductId())));

                priceTotalOrder += calculatePriceOrder(
                        accessories.getAmount(), accessoryService.getPriceAccessoryById(accessories.getProductId()));

                // Modificar el stock del 'Accessory' -> Hacer las querys en el repositorio.

                purchase.addAccessoryPurchases(accessoryPurchase);
                accessoryPurchaseService.saveAccessoryPurchase(accessoryPurchase);
            }
        }

        purchase.setPriceOrder(priceTotalOrder);
        purchaseService.savePurchase(purchase);

        return new ResponseEntity<>("Order recieved", HttpStatus.CREATED);

    }


}
