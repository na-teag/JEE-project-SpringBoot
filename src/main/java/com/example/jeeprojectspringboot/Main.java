package com.example.jeeprojectspringboot;

import com.example.jeeprojectspringboot.service.AddressService;
import com.example.jeeprojectspringboot.service.DataCleanupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Main extends SpringBootServletInitializer implements CommandLineRunner {


	@Autowired
	private DataCleanupService dataCleanupService;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Main.class);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length > 0 && "reset".equalsIgnoreCase(args[0])) {
			resetDatabase();
		}
	}

	public void resetDatabase(){
		dataCleanupService.deleteAllEntities(); // Supprimer toutes les instances
		dataCleanupService.fillDatabase(); // créer celles par défaut
		// pour un reset lancez : mvn spring-boot:run -Dspring-boot.run.arguments=reset
	}

}