package com.learning.ddd.onlinestore.inventory_management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( {"com.learning.ddd.onlinestore.*"} )
public class InventoryManagementServiceTestBootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(InventoryManagementServiceTestBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementServiceTestBootApp.class, args);
	}

}

