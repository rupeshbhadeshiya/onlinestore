package com.learning.ddd.onlinestore.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication //(scanBasePackages = {"com.learning.ddd.*"})
public class OrderServiceBootApp { //extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(OrderServiceBootApp.class, args);
	}
	
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(OrderServiceBootApp.class);
//	}
	

}

