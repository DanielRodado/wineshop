package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.AccessoryPurchase;
import com.wineshop.ecommerce.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface AccessoryPurchaseRepository extends JpaRepository<AccessoryPurchase, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM AccessoryPurchase ap WHERE ap.purchase = :purchaseParam")
    void deleteAccessoryPurchasesByPurchase(@Param("purchaseParam") Purchase purchase);

}
