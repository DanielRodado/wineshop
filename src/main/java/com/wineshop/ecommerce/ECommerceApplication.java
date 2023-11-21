package com.wineshop.ecommerce;

import com.wineshop.ecommerce.models.Client;
import com.wineshop.ecommerce.models.Purchase;
import com.wineshop.ecommerce.repositories.ClientRepository;
import com.wineshop.ecommerce.repositories.PurchaseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, PurchaseRepository purchaseRepository) {
		return args -> {

			Client clientOne = new Client("Daniel", "Rodado", "pass123", "daniel@gmail.com",
					true, LocalDate.of(2005,7, 14));
			clientRepository.save(clientOne);

			Purchase order = new Purchase("Carrera 17 NO 23-54", LocalDate.now());
			clientOne.addPurchase(order);
			purchaseRepository.save(order);

		};
	}

}
