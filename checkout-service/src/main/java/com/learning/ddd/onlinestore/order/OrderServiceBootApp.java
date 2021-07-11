package com.learning.ddd.onlinestore.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication
public class OrderServiceBootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OrderServiceBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceBootApp.class, args);
	}

}

