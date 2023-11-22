package com.wineshop.ecommerce.services;

import com.wineshop.ecommerce.models.Wine;

public interface WineService {

    boolean existsWineById(Long id);

    Double getPriceWineById(Long id);

    boolean existsWineByIdAndStockGreaterThan(Long id, int amount);

    void updateStockWineById(Long id, int amount);

    Wine findWineById(Long id);

    void saveWine(Wine wine);

}
