package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    @Query("SELECT a.price FROM Accessory a WHERE a.id = :accessoryId")
    Double getPriceAccessoryById(@Param("accessoryId") Long id);

}
