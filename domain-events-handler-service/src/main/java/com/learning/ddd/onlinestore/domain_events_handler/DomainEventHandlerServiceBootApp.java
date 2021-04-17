package com.learning.ddd.onlinestore.domain_events_handler;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Listen to topics where Domain Events are published by other Microservices
// Store them to a Data store (say MongoDB)
// Publish APIs to read these stored domain events

@ComponentScan("com.learning.ddd.onlinestore")
@SpringBootApplication
@RestController
@RequestMapping("/domain-events")
public class DomainEventHandlerServiceBootApp { //must be in root package of project
	
	public static void main(String[] args) {
		SpringApplication.run(DomainEventHandlerServiceBootApp.class, args);
	}
	
	@GetMapping("/number") // a ping kind of api to know things working fine!
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
}
