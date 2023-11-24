package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.dto.AccessoryDTO;
import com.wineshop.ecommerce.models.Accessory;
import com.wineshop.ecommerce.repositories.AccessoryRepository;
import com.wineshop.ecommerce.services.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccessoryImplement implements AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;

    @Override
    public Set<Accessory> getAllAccessories() {
        return new HashSet<>(accessoryRepository.findAll());
    }

    @Override
    public Set<AccessoryDTO> getAllAccessoriesDTO() {
        return getAllAccessories().stream().map(AccessoryDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Double getPriceAccessoryById(Long id) {
        return accessoryRepository.getPriceAccessoryById(id);
    }

    @Override
    public boolean existsAccessoryById(Long id) {
        return accessoryRepository.existsById(id);
    }

    @Override
    public boolean existsAccessoryByIdAndStockGreaterThan(Long id, int amount) {
        return accessoryRepository.existsByIdAndStockGreaterThan(id, amount);
    }

    @Override
    public Accessory findAccessoryById(Long id) {
        return accessoryRepository.findById(id).orElse(null);
    }

    @Override
    public void updateStockAccessoryById(Long id, int amount) {
        accessoryRepository.updateStockAccessoryById(id, amount);
    }

    @Override
    public void saveAccessory(Accessory accessory) {
        accessoryRepository.save(accessory);
    }
}
