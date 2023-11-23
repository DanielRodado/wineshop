package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.dto.AccessoryDTO;
import com.wineshop.ecommerce.models.Accessory;

import java.util.Set;

public interface AccessoryService {

    Set<Accessory> getAllAccessories();

    Set<AccessoryDTO> getAllAccessoriesDTO();

    Double getPriceAccessoryById(Long id);

    boolean existsAccessoryById(Long id);

    boolean existsAccessoryByIdAndStockGreaterThan(Long id, int amount);

    Accessory findAccessoryById(Long id);

    void updateStockAccessoryById(Long id, int amount);

    void saveAccessory(Accessory accessory);

}
