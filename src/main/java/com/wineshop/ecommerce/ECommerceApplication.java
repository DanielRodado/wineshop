package com.wineshop.ecommerce;

import com.wineshop.ecommerce.models.*;
import com.wineshop.ecommerce.repositories.AccessoryRepository;
import com.wineshop.ecommerce.repositories.ClientRepository;
import com.wineshop.ecommerce.repositories.PurchaseRepository;
import com.wineshop.ecommerce.repositories.WineRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, PurchaseRepository purchaseRepository,
									  WineRepository wineRepository, AccessoryRepository accessoryRepository) {
		return args -> {

			Client clientOne = new Client("Daniel", "Rodado", "pass123", "daniel@gmail.com",
					true, LocalDate.of(2005,7, 14));
			clientRepository.save(clientOne);

			Wine wineOne = new Wine("Vinos Daniel", "Es un vino", "Barranquilla", "Las Nieves", 22.6, List.of("add",
					"ass.com"), 100, 750, Variety.MALBEC, WineType.SPARKLING);
			wineRepository.save(wineOne);

			Wine wineTwo = new Wine("Vino Montanya", "Puede ser bueno", "Medallo", "El lago",
					11.2, List.of("ass.com", "www.img.com"), 210, 300, Variety.CARMENERE, WineType.WINE);
			wineRepository.save(wineTwo);

			Accessory accessoryOne = new Accessory("Decanter", "I's a glass decanter", List.of("imagen1.url", "imagen2.url"),
					25.5, 50);
			accessoryRepository.save(accessoryOne);

			Accessory accessoryTwo = new Accessory("Metallic corkscrew", "I's a corkscrew", List.of("imagen3.url", "imagen4.url"),
					9.95, 25);
			accessoryRepository.save(accessoryTwo);

		};
	}

}
