package com.wineshop.ecommerce.utils;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wineshop.ecommerce.dto.ProductRecieverDTO;
import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.services.AccessoryPurchaseService;
import com.wineshop.ecommerce.services.PurchaseService;
import com.wineshop.ecommerce.services.WinePurchaseService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

import static java.awt.Color.BLACK;

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
