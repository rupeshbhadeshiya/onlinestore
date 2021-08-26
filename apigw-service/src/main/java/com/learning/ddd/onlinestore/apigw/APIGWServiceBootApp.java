package com.learning.ddd.onlinestore.apigw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@EnableZuulProxy
@EnableEurekaClient
@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication
public class APIGWServiceBootApp  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(APIGWServiceBootApp.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(APIGWServiceBootApp.class);
	}
	
}

