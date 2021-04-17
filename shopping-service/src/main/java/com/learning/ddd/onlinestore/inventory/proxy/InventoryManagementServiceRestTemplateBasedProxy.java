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

import com.learning.ddd.onlinestore.shopping.domain.Item;

@Component
public class InventoryManagementServiceRestTemplateBasedProxy {

	@Autowired
	private RestTemplate restTemplate;
	
	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	@GetMapping("/inventory/items")
	public List<Item> getItems() {
		
		List<Item> response = restTemplate.exchange(
			"http://inventory-management-service/inventory/items", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Item>>() {}
		).getBody();
		
		System.out.println("~~~~~~~~~~~~~~~~~~> Response Received : " + response);
		
		return response;
	}
	
	@PostMapping("/inventory/items")
	public List<Item> addItems(List<Item> items) {
		
		List<Item> response = restTemplate.exchange(
			"http://inventory-management-service/inventory/items", 
			HttpMethod.POST,
			null,
			new ParameterizedTypeReference<List<Item>>() {}
		).getBody();
		
		return response;
		
//		ResponseEntity responseEntity = restTemplate.postForObject(
//			"http://inventory-management-service/inventory/items", 
//			items, 
//			ResponseEntity.class, 
//			new HashMap<>()
//		);
//		return (List<Item>) responseEntity.getBody();
	}

}
