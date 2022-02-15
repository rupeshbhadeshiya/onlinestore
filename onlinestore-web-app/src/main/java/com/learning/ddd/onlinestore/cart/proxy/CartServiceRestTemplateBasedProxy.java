package com.learning.ddd.onlinestore.cart.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.cart.application.dto.AddItemToCartDTO;
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
	
	public Cart addItemToCart(int cartId, InventoryItem inventoryItem) {
		
		AddItemToCartDTO dto = new AddItemToCartDTO(CONSUMER_ID, cartId, 
			ItemConversionUtil.fromInventoryItemToCartItem(inventoryItem));
		
		HttpEntity<AddItemToCartDTO> request = new HttpEntity<AddItemToCartDTO>(dto);
		
		Cart cart = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + CONSUMER_ID + "/carts",
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<Cart>() {}
		).getBody();
		
		return cart;
	}
	
//	public List<Cart> getAllCarts() {
//		
//		List<Cart> allCarts = cartServiceRestTemplate.exchange(
//			"http://cart-service/consumers/" + CONSUMER_ID + "/carts", 
//			HttpMethod.GET,
//			null,
//			new ParameterizedTypeReference<List<Cart>>() {}
//		).getBody();
//		
//		return allCarts;
//	}
	
	public Cart getCart(String consumerId) {
		
		List<Cart> carts = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + consumerId + "/carts", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Cart>>() {}
		).getBody();
		
		return carts.isEmpty() ? null : carts.get(0); // every consumer have at max one cart only!
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

	public Cart removeItemFromCart(String consumerId, int cartId, int itemId) {
		
		Cart cart = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + consumerId + "/carts/" + cartId + "/items/" + itemId,
			HttpMethod.DELETE,
			null,
			new ParameterizedTypeReference<Cart>() {}
		).getBody();
		
		return cart;
	}

	public void emptyCart(String consumerId, int cartId) {
		
		cartServiceRestTemplate.exchange(
				"http://cart-service/consumers/" + consumerId + "/carts/" + cartId, 
			HttpMethod.DELETE,
			null,
			new ParameterizedTypeReference<Cart>() {}
		);
	}

}
