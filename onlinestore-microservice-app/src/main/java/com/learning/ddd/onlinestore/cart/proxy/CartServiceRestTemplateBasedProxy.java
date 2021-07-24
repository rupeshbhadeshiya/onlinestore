package com.learning.ddd.onlinestore.cart.proxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

@Component
public class CartServiceRestTemplateBasedProxy {

	@Autowired
	private RestTemplate restTemplate;
	
	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	
	@PostMapping("/cart-service/carts")
	public Cart addCart() throws URISyntaxException {
		
		//MyRequest body = ...
		
		InventoryItem BISCUIT_ITEM = new InventoryItem(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
		InventoryItem CHIVDA_ITEM = new InventoryItem(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
		InventoryItem BATHING_SOAP_ITEM = new InventoryItem(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
		InventoryItem PENCIL_ITEM = new InventoryItem(302, "Stationery", "Pencil", "Natraj Pencil", 10, 5.0);
		
		List<InventoryItem> items = new ArrayList<>();
		items.add(BISCUIT_ITEM);
		items.add(CHIVDA_ITEM);
		items.add(BATHING_SOAP_ITEM);
		items.add(PENCIL_ITEM);
		
		RequestEntity<List<InventoryItem>> request = RequestEntity
		    .post(new URI("http://cart-service/carts"))
		    .accept(MediaType.APPLICATION_JSON)
		    .body(items);
		
		ResponseEntity<Cart> response = restTemplate
			.exchange(request, Cart.class);
		 
//		Cart response = restTemplate.exchange(
//			"http://cart-service/carts", 
//			HttpMethod.POST,
//			null,
//			new ParameterizedTypeReference<Cart>() {}
//		).getBody();
		
		System.out.println("~~~~~~~~~~~~~> Response Received : " + response);
		
		return response.getBody();
	}
	
	
	@GetMapping("/cart-service/carts")
	public List<Cart> getCarts() {
		
		List<Cart> response = restTemplate.exchange(
			"http://cart-service/carts", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Cart>>() {}
		).getBody();
		
		System.out.println("~~~~~~~~~~~~~> Response Received : " + response);
		
		return response;
	}
	
}
