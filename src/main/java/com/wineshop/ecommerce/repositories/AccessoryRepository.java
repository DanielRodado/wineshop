package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    @Query("SELECT a.price FROM Accessory a WHERE a.id = :accessoryId")
    Double getPriceAccessoryById(@Param("accessoryId") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Accessory a SET a.stock = a.stock - :accessoryAmount WHERE a.id = :accessoryId")
    void updateStockAccessoryById(@Param("accessoryId") Long id, @Param("accessoryAmount") int amount);

    boolean existsByIdAndStockGreaterThan(Long id, int amount);

    @Query("SELECT a.name FROM Accessory a WHERE a.id = :accessoryId")
    String getNameById(@Param("accessoryId")Long id);
}
