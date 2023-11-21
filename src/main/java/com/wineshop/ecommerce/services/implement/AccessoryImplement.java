package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.repositories.AccessoryRepository;
import com.wineshop.ecommerce.services.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessoryImplement implements AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;

    @Override
    public Double getPriceAccessoryById(Long id) {
        return accessoryRepository.getPriceAccessoryById(id);
    }
}
