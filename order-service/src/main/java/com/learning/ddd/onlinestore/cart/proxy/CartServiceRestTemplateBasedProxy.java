package com.learning.ddd.onlinestore.cart.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.cart.application.dto.CartInfo;

@Component
public class CartServiceRestTemplateBasedProxy {

	// FIXME Once user mgmt is in place replace this with actual/runtime value
	private static final String CONSUMER_ID = "11";
	
	@Autowired
	private RestTemplate cartServiceRestTemplate;
	
	@Bean(name = "cartServiceRestTemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	public CartInfo getCartInfo(int cartId) {
		
		CartInfo CartInfo = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + CONSUMER_ID + "/carts/" + cartId, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<CartInfo>() {}
		).getBody();
		
		return CartInfo;
	}

//	public Cart removeItemFromCart(String consumerId, int cartId, int itemId) {
//		
//		Cart cart = cartServiceRestTemplate.exchange(
//			"http://cart-service/consumers/" + consumerId + "/carts/" + cartId + "/items/" + itemId,
//			HttpMethod.DELETE,
//			null,
//			new ParameterizedTypeReference<Cart>() {}
//		).getBody();
//		
//		return cart;
//	}
//
//	public void emptyCart(String consumerId, int cartId) {
//		
//		cartServiceRestTemplate.exchange(
//				"http://cart-service/consumers/" + consumerId + "/carts/" + cartId, 
//			HttpMethod.DELETE,
//			null,
//			new ParameterizedTypeReference<Cart>() {}
//		);
//	}

}
