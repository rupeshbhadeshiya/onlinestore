package com.learning.ddd.onlinestore.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication //(scanBasePackages = "com.learning.ddd.onlinestore")
public class OnlineStoreWebAppSpringBootApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreWebAppSpringBootApplication.class, args);
    }
    
    @Bean //(name = "inventoryServiceRestTemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
}
