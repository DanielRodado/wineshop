package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.models.Accessory;

public interface AccessoryService {

    Double getPriceAccessoryById(Long id);

    boolean existsAccessoryById(Long id);

    boolean existsAccessoryByIdAndStockGreaterThan(Long id, int amount);

    Accessory findAccessoryById(Long id);

    void updateStockAccessoryById(Long id, int amount);

}
