package com.learning.ddd.onlinestore.inventory.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.cart.domain.CartItem;

@Component
public class InventoryServiceRestTemplateBasedProxy {

	@Autowired
	private RestTemplate restTemplate;
	
	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	@GetMapping("/inventory/items")
	public List<CartItem> getItems() {
		
		List<CartItem> response = restTemplate.exchange(
			"http://inventory-service/items", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<CartItem>>() {}
		).getBody();
		
		System.out.println("~~~~~~~~~~~~~~~~~~> Response Received : " + response);
		
		return response;
	}
	
	@PostMapping("/inventory/items")
	public List<CartItem> addItems(List<CartItem> items) {
		
		List<CartItem> response = restTemplate.exchange(
			"http://inventory-service/items", 
			HttpMethod.POST,
			null,
			new ParameterizedTypeReference<List<CartItem>>() {}
		).getBody();
		
		return response;
		
//		ResponseEntity responseEntity = restTemplate.postForObject(
//			"http://inventory-service/items", 
//			items, 
//			ResponseEntity.class, 
//			new HashMap<>()
//		);
//		return (List<Item>) responseEntity.getBody();
	}

}
