package com.learning.ddd.onlinestore.microservice_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(scanBasePackages = { "com.learning.ddd.onlinestore" })
public class OnlineStoreMicroservicesAppBootApp {
	
	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreMicroservicesAppBootApp.class, args);
	}
	
}