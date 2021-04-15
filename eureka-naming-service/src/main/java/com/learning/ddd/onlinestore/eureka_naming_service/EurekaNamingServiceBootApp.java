package com.learning.ddd.onlinestore.eureka_naming_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaNamingServiceBootApp {
	
	public static void main(String[] args) {
		SpringApplication.run(EurekaNamingServiceBootApp.class, args);
	}
	
}
