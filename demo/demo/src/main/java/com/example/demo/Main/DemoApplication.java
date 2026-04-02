package com.example.demo.Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EntityScan("com.example.demo.*")
@EnableJpaRepositories(
		basePackages = "com.example.demo.*",
		entityManagerFactoryRef = "entityManagerFactory",
		enableDefaultTransactions = false
)
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
