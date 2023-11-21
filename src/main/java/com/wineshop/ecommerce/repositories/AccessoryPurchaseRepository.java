package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.AccessoryPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccessoryPurchaseRepository extends JpaRepository<AccessoryPurchase, Long> {
}
