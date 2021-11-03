package com.learning.ddd.onlinestore.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@ComponentScan("com.learning.ddd.onlinestore")
@SpringBootApplication
public class InventoryServiceBootApp {
	

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceBootApp.class, args);
	}
	
	
}
