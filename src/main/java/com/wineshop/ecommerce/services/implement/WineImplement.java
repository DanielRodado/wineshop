package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.repositories.WineRepository;
import com.wineshop.ecommerce.services.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WineImplement implements WineService {

    @Autowired
    private WineRepository wineRepository;

    @Override
    public boolean existsWineById(Long id) {
        return wineRepository.existsById(id);
    }

    @Override
    public Double getPriceWineById(Long id) {
        return wineRepository.getPriceWineById(id);
    }
}
