package com.learning.ddd.onlinestore.shopping.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( {"com.learning.ddd.onlinestore.*"} )
public class ShoppingServiceSpringBootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShoppingServiceSpringBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ShoppingServiceSpringBootApp.class, args);
	}

}

