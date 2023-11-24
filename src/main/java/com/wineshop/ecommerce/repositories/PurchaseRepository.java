package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
