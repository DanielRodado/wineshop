package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.dto.ClientDTO;
import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p.priceOrder FROM Purchase p WHERE id = :purchaseId")
    Double getPriceOrderById(@Param("purchaseId") Long id);

    Boolean existsByIdAndClient(Long purchaseId, Client client);

    Set<Purchase> getPurchasesByClient(Client client);
}
