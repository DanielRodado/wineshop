package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface WineRepository extends JpaRepository<Wine, Long> {
}
