package com.learning.ddd.onlinestore.inventory.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( {"com.learning.ddd.onlinestore.*"} )
public class InventoryManagementServiceSpringBootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(InventoryManagementServiceSpringBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementServiceSpringBootApp.class, args);
	}

}

