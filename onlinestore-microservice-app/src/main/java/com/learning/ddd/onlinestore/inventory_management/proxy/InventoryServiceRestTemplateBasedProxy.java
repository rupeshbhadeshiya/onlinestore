package com.learning.ddd.onlinestore.inventory_management.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

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
	public List<InventoryItem> getItems() {
		
		List<InventoryItem> response = restTemplate.exchange(
			"http://inventory-service/items", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<InventoryItem>>() {}
		).getBody();
		
		System.out.println("~~~~~~~~~~~~~> Response Received : " + response);
		
		return response;
	}
	
//	@PostMapping("/inventory/items")
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
//			"http://inventory-service/items", 
//			HttpMethod.GET,
//			null,
//			Void.class,
//			new HashMap<>()
//		);
//		
//	}

}
