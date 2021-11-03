package com.learning.ddd.onlinestore.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication//(scanBasePackages = "com.learning.ddd.onlinestore.*")
public class CartServiceBootApp { // extends SpringBootServletInitializer {

	
	public static void main(String[] args) {
		SpringApplication.run(CartServiceBootApp.class, args);
	}
	
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(CartServiceBootApp.class);
//	}
	
	
}

