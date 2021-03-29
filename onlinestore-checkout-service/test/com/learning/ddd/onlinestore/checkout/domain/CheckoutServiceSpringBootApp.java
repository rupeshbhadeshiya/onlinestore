package com.learning.ddd.onlinestore.checkout.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( {"com.learning.ddd.onlinestore.*"} )
public class CheckoutServiceSpringBootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CheckoutServiceSpringBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CheckoutServiceSpringBootApp.class, args);
	}

}

