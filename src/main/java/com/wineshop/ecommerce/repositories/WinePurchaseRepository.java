package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.models.WinePurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface WinePurchaseRepository extends JpaRepository<WinePurchase, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM WinePurchase wp WHERE wp.purchase = :purchaseParam")
    void deleteWinePurchasesByPurchase(@Param("purchaseParam") Purchase purchase);

}
