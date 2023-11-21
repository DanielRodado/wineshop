package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface WineRepository extends JpaRepository<Wine, Long> {

    @Query("SELECT w.price FROM Wine w WHERE w.id = :wineId")
    Double getPriceWineById(@Param("wineId") Long id);

}
