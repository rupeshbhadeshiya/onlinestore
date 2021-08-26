package com.learning.ddd.onlinestore.cart.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.cart.application.dto.PullCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.commons.util.ItemConversionUtil;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

@Component
public class CartServiceRestTemplateBasedProxy {

	// FIXME Once user mgmt is in place replace this with actual/runtime value
	private static final String CONSUMER_ID = "11";
	
	@Autowired
	private RestTemplate cartServiceRestTemplate;
	
//	@Bean(name = "cartServiceRestTemplate")
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	
	
	public Cart pullCartAndAddItem(InventoryItem inventoryItem) {
		
		PullCartDTO dto = new PullCartDTO(CONSUMER_ID);
		dto.addItem(ItemConversionUtil.fromInventoryItemToCartItem(inventoryItem));
		
		HttpEntity<PullCartDTO> request = new HttpEntity<PullCartDTO>(dto);
		
		Cart cart = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + CONSUMER_ID + "/carts", 
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<Cart>() {}
		).getBody();
		
		return cart;
	}
	
	public Cart shopItem(int cartId, InventoryItem inventoryItem) {
		
		PullCartDTO dto = new PullCartDTO(CONSUMER_ID);
		dto.addItem(ItemConversionUtil.fromInventoryItemToCartItem(inventoryItem));
		
		HttpEntity<PullCartDTO> request = new HttpEntity<PullCartDTO>(dto);
		
		Cart cart = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + CONSUMER_ID + "/carts/" + cartId, 
			HttpMethod.PUT,
			request,
			new ParameterizedTypeReference<Cart>() {}
		).getBody();
		
		return cart;
	}
	
	public List<Cart> getAllCarts() {
		
		List<Cart> allCarts = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + CONSUMER_ID + "/carts", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Cart>>() {}
		).getBody();
		
		return allCarts;
	}
	
	public Cart getCart(Integer cartId) {
		
		Cart cart = cartServiceRestTemplate.exchange(
				"http://cart-service/consumers/" + CONSUMER_ID + "/carts/" + cartId, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<Cart>() {}
		).getBody();
		
		return cart;
	}


	public void emptyCart(Integer cartId) {
		
		cartServiceRestTemplate.exchange(
				"http://cart-service/consumers/" + CONSUMER_ID + "/carts/" + cartId, 
			HttpMethod.DELETE,
			null,
			new ParameterizedTypeReference<Cart>() {}
		);
	}

}
