package com.wineshop.ecommerce.utils;

import com.wineshop.ecommerce.dto.ProductRecieverDTO;
import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.services.AccessoryPurchaseService;
import com.wineshop.ecommerce.services.PurchaseService;
import com.wineshop.ecommerce.services.WinePurchaseService;

import java.util.Set;

public final class PurchaseUtil {

    public static void deletePurchaseAndProductPurchase(WinePurchaseService winePurchaseService,
                                                        AccessoryPurchaseService accessoryPurchaseService,
                                                        PurchaseService purchaseService,
                                                        Set<ProductRecieverDTO> wines,
                                                        Set<ProductRecieverDTO> accessories, Purchase purchase) {

        if (!wines.isEmpty()) {
            winePurchaseService.deleteWinePurchasesByPurchase(purchase);
        }

        // Deleted Accessories
        if (!accessories.isEmpty()) {
            accessoryPurchaseService.deleteAccessoryPurchasesByPurchase(purchase);
        }

        // Deleted Purchase
        purchaseService.deletePurchaseById(purchase.getId());

    }

}
