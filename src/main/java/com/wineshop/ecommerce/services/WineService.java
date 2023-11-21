package com.wineshop.ecommerce.services;

public interface WineService {

    boolean existsWineById(Long id);

    Double getPriceWineById(Long id);

}
