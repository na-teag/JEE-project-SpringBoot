package com.example.jeeprojectspringboot;

import com.example.jeeprojectspringboot.schoolmanager.Address;
import com.example.jeeprojectspringboot.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JeeProjectSpringBootApplication implements CommandLineRunner {

	@Autowired
	private AddressService addressService; // Injection du service

	public static void main(String[] args) {
		SpringApplication.run(JeeProjectSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Créer une nouvelle adresse à ajouter
		Address address = new Address();
		address.setNumber("123");
		address.setStreet("Rue des Lilas");
		address.setCity("Paris");
		address.setPostalCode(75000);
		address.setCountry("France");

		// Sauvegarder l'adresse dans la base de données via le service
		Address savedAddress = addressService.addAddress(address);

		// Afficher l'adresse ajoutée
		System.out.println("Adresse ajoutée : " + savedAddress);
	}

}
