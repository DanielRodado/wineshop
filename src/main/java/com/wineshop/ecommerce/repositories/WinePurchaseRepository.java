package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.WinePurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface WinePurchaseRepository extends JpaRepository<WinePurchase, Long> {
}
