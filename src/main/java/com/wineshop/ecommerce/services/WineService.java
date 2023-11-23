package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.dto.WineDTO;
import com.wineshop.ecommerce.models.Wine;

import java.util.Set;

public interface WineService {

    Set<Wine> getAllWines();

    Set<WineDTO> getAllWinesDTO();

    boolean existsWineById(Long id);

    Double getPriceWineById(Long id);

    boolean existsWineByIdAndStockGreaterThan(Long id, int amount);

    void updateStockWineById(Long id, int amount);

    Wine findWineById(Long id);

    void saveWine(Wine wine);

}
