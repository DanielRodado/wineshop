package com.wineshop.ecommerce.repositories;

import com.wineshop.ecommerce.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail(String email);

    boolean existsByEmail(String email);

}
