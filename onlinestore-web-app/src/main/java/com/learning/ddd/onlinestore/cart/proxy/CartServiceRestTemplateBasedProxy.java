package com.learning.ddd.onlinestore.cart.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.cart.application.dto.AddProductToCartDTO;
import com.learning.ddd.onlinestore.cart.application.dto.CartInfo;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.inventory.domain.Product;

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
	
	public Cart addProductToCart(int cartId, Product product) {
		
		AddProductToCartDTO dto = new AddProductToCartDTO(CONSUMER_ID, cartId, product);
		
		HttpEntity<AddProductToCartDTO> request = new HttpEntity<AddProductToCartDTO>(dto);
		
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
	
	public CartInfo getCartInfo(Integer cartId) {
		
		CartInfo cartInfo = cartServiceRestTemplate.exchange(
				"http://cart-service/consumers/" + CONSUMER_ID + "/carts/" + cartId, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<CartInfo>() {}
		).getBody();
		
		return cartInfo;
	}

	public Cart removeProductFromCart(String consumerId, int cartId, int productId) {
		
		Cart cart = cartServiceRestTemplate.exchange(
			"http://cart-service/consumers/" + consumerId + "/carts/" + cartId + "/products/" + productId,
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
