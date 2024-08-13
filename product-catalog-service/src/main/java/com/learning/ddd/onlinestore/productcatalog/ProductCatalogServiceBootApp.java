package com.learning.ddd.onlinestore.productcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableEurekaClient
@ComponentScan( {"com.learning.ddd.onlinestore"} )
//this is required find repository classes like CartRepository etc classes in other package com.learning.ddd.onlinestore.cart.*
@EnableJpaRepositories( {"com.learning.ddd.onlinestore"} )
//this is required to find entity classes like Cart/CartItem etc classes in other package com.learning.ddd.onlinestore.cart.*
@EntityScan( {"com.learning.ddd.onlinestore"} ) 
@SpringBootApplication //(scanBasePackages = {"com.learning.ddd.*"})
public class ProductCatalogServiceBootApp {
	

	public static void main(String[] args) {
		SpringApplication.run(ProductCatalogServiceBootApp.class, args);
	}
	
	
}
