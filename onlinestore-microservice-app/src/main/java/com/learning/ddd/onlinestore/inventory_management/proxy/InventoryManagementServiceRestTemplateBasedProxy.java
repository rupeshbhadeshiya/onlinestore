package com.learning.ddd.onlinestore.inventory_management.proxy;

import java.util.HashMap;
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
	
	@GetMapping("/inventory-management/items")
	public List<Item> getItems() {
		
		List<Item> response = restTemplate.exchange(
			"http://inventory-management-service/items", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Item>>() {}
		).getBody();
		
		System.out.println("~~~~~~~~~~~~~> Response Received : " + response);
		
		return response;
	}
	
//	@PostMapping("/inventory-management/items")
//	public void addItems(List<Item> items) {
//		
////		MyRequest body = ...
////		RequestEntity request = RequestEntity
////		    .post(new URI("https://example.com/foo"))
////		    .accept(MediaType.APPLICATION_JSON)
////		    .body(body);
////		ResponseEntity<MyResponse> response = template.exchange(request, MyResponse.class);
//				 
//		restTemplate.exchange(
//			"http://inventory-management-service/items", 
//			HttpMethod.GET,
//			null,
//			Void.class,
//			new HashMap<>()
//		);
//		
//	}

}
