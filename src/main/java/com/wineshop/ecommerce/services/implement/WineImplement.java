package com.wineshop.ecommerce.services.implement;

import com.wineshop.ecommerce.dto.WineDTO;
import com.wineshop.ecommerce.models.Wine;
import com.wineshop.ecommerce.repositories.WineRepository;
import com.wineshop.ecommerce.services.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WineImplement implements WineService {

    @Autowired
    private WineRepository wineRepository;

    @Override
    public Set<Wine> getAllWines() {
        return new HashSet<>(wineRepository.findAll());
    }

    @Override
    public Set<WineDTO> getAllWinesDTO() {
        return getAllWines().stream().map(WineDTO::new).collect(Collectors.toSet());
    }

    @Override
    public boolean existsWineById(Long id) {
        return wineRepository.existsById(id);
    }

    @Override
    public Double getPriceWineById(Long id) {
        return wineRepository.getPriceWineById(id);
    }

    @Override
    public boolean existsWineByIdAndStockGreaterThan(Long id, int amount) {
        return wineRepository.existsByIdAndStockGreaterThan(id, amount);
    }

    @Override
    public void updateStockWineById(Long id, int amount) {
        wineRepository.updateStockWineById(id, amount);
    }

    @Override
    public Wine findWineById(Long id) {
        return wineRepository.findById(id).orElse(null);
    }

    @Override
    public void saveWine(Wine wine) {
        wineRepository.save(wine);
    }

    @Override
    public String getWineNameById(Long id) {
        return wineRepository.getNameById(id);
    }
}
