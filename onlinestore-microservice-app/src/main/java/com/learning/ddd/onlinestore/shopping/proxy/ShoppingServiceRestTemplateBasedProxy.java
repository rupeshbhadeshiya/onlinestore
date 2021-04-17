package com.learning.ddd.onlinestore.shopping.proxy;

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

import com.learning.ddd.onlinestore.inventory.domain.Item;
import com.learning.ddd.onlinestore.shopping.domain.Cart;

@Component
public class ShoppingServiceRestTemplateBasedProxy {

	@Autowired
	private RestTemplate restTemplate;
	
	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	
	@PostMapping("/shopping/carts")
	public Cart addCart() throws URISyntaxException {
		
		//MyRequest body = ...
		
		Item BISCUIT_ITEM = new Item(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
		Item CHIVDA_ITEM = new Item(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
		Item BATHING_SOAP_ITEM = new Item(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
		Item PENCIL_ITEM = new Item(302, "Stationery", "Pencil", "Natraj Pencil", 10, 5.0);
		
		List<Item> items = new ArrayList<>();
		items.add(BISCUIT_ITEM);
		items.add(CHIVDA_ITEM);
		items.add(BATHING_SOAP_ITEM);
		items.add(PENCIL_ITEM);
		
		RequestEntity request = RequestEntity
		    .post(new URI("http://shopping-service/carts"))
		    .accept(MediaType.APPLICATION_JSON)
		    .body(items);
		
		ResponseEntity<Cart> response = restTemplate
			.exchange(request, Cart.class);
		 
//		Cart response = restTemplate.exchange(
//			"http://shopping-service/carts", 
//			HttpMethod.POST,
//			null,
//			new ParameterizedTypeReference<Cart>() {}
//		).getBody();
		
		System.out.println("~~~~~~~~~~~~~> Response Received : " + response);
		
		return response.getBody();
	}
	
	
	@GetMapping("/shopping/carts")
	public List<Cart> getCarts() {
		
		List<Cart> response = restTemplate.exchange(
			"http://shopping-service/carts", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Cart>>() {}
		).getBody();
		
		System.out.println("~~~~~~~~~~~~~> Response Received : " + response);
		
		return response;
	}
	
}
