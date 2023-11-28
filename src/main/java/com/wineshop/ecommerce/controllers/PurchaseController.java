package com.wineshop.ecommerce.controllers;

import com.wineshop.ecommerce.dto.NewPurchaseApplicationDTO;
import com.wineshop.ecommerce.dto.ProductRecieverDTO;
import com.wineshop.ecommerce.models.*;
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

    @Autowired
    private ClientService clientService;

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
                // El ID de los 'Wines' exista -> Services;
                if (!wineService.existsWineById(wine.getProductId())) {
                    return new ResponseEntity<>("One of the wines you are trying to buy doesn't exist.",
                            HttpStatus.FORBIDDEN);
                }
                // La cantidad de Wine a comprar no sea menor o igual a 0;
                if (wine.getAmount() <= 0) {
                    return new ResponseEntity<>("The amount of wines that you are trying to buy cannot be 0", HttpStatus.FORBIDDEN);
                }

                // Comprobar si la cantidad a comprar no supere el stock del 'Wine';
                if (!wineService.existsWineByIdAndStockGreaterThan(wine.getProductId(), wine.getAmount())) {
                    return new ResponseEntity<>("Right now, we don't have wines in stock to supply your order.",
                            HttpStatus.FORBIDDEN);
                }

                WinePurchase winePurchase = new WinePurchase(
                        wine.getAmount(), calculatePriceOrder(
                                wine.getAmount(), wineService.getPriceWineById(wine.getProductId())));

                winePurchase.setWine(wineService.findWineById(wine.getProductId()));

                priceTotalOrder += calculatePriceOrder(
                        wine.getAmount(), wineService.getPriceWineById(wine.getProductId()));

                // Modificar el stock del 'Wine' -> Hacer las querys en el repositorio.
                wineService.updateStockWineById(wine.getProductId(), wine.getAmount());
                purchase.addWinePurchase(winePurchase);
                winePurchaseService.saveWinePurchase(winePurchase);
            }
        }


        if (!newPurchaseApp.getAccessories().isEmpty()) {

            for (ProductRecieverDTO accessory : newPurchaseApp.getAccessories()) {
                // Comprobar que el ID de los 'Wines' exista -> Services;
                if (!accessoryService.existsAccessoryById(accessory.getProductId())) {
                    return new ResponseEntity<>("One of the accessories you are trying to buy doesn't exist.",
                            HttpStatus.FORBIDDEN);
                }

                // Comprobar que la cantidad de Wine a comprar no sea menor o igual a 0;
                if (accessory.getAmount() <= 0) {
                    return new ResponseEntity<>("The amount of accessories that you are trying to buy cannot be 0",
                            HttpStatus.FORBIDDEN);
                }

                // Comprobar si la cantidad a comprar no supere el stock del 'Wine';
                if (!accessoryService.existsAccessoryByIdAndStockGreaterThan(accessory.getProductId(),
                        accessory.getAmount())) {
                    return new ResponseEntity<>("Right now, we don't have accessories in stock to supply your order.",
                            HttpStatus.FORBIDDEN);
                }

                AccessoryPurchase accessoryPurchase = new AccessoryPurchase(
                        accessory.getAmount(), calculatePriceOrder(
                        accessory.getAmount(), accessoryService.getPriceAccessoryById(accessory.getProductId())));

                accessoryPurchase.setAccessory(accessoryService.findAccessoryById(accessory.getProductId()));

                priceTotalOrder += calculatePriceOrder(
                        accessory.getAmount(), accessoryService.getPriceAccessoryById(accessory.getProductId()));

                // Modificar el stock del 'Accessory' -> Hacer las querys en el repositorio.
                accessoryService.updateStockAccessoryById(accessory.getProductId(), accessory.getAmount());

                purchase.addAccessoryPurchases(accessoryPurchase);
                accessoryPurchaseService.saveAccessoryPurchase(accessoryPurchase);
            }
        }

        purchase.setPriceOrder(priceTotalOrder);
        Client client = clientService.findClientById(1L);
        client.addPurchase(purchase);
        purchaseService.savePurchase(purchase);

        return new ResponseEntity<>("Order recieved", HttpStatus.CREATED);

    }

}