package com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.learning.ddd.onlinestore.commons.util.HttpUtil;

@TestMethodOrder(OrderAnnotation.class)
class CartCartAPITest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final CartCartItem BISCUIT_ITEM = new CartCartItem("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final CartCartItem BATHING_SOAP_ITEM = new CartCartItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
//	@Test
//	@Order(1)
//	void pullCartAndAddItems() throws IOException {
//		
//		PullCartDTO dto = new PullCartDTO();
//		dto.setCartItemName(CART_ITEM_NAME);
//		
//		Cart cart = (Cart) 
//			HttpUtil.post("http://localhost:9009/commons/spike/carts", dto, Cart.class);
//		
//		assertNotNull(cart);
//		assertNotNull(cart.getItems());
//		assertEquals(1, cart.getItems().size());
//		assertEquals(CART_ITEM_NAME, cart.getItems().get(0).getName());
//	}
//	
//	@Test
//	@Order(2)
//	void getAllCartsAndTheirCartItems() throws IOException {
//		
//		Cart[] carts = (Cart[]) 
//			HttpUtil.get("http://localhost:9009/commons/spike/carts",Cart[].class);
//		
//		assertNotNull(carts);
//		assertEquals(1, carts.length);
//		Cart cart = carts[0];
//		assertNotNull(cart);
//		assertNotNull(cart.getItems());
//		assertEquals(1, cart.getItems().size());
//		assertEquals(CART_ITEM_NAME, cart.getItems().get(0).getName());
//	}

	@Test
	@org.junit.jupiter.api.Order(1)
	void pullCartAndAddItems() throws IOException {
		
		// prepare
		
		PullCartCartDTO dto = new PullCartCartDTO(CONSUMER_ID);
		dto.addItem(BISCUIT_ITEM);
		dto.addItem(BATHING_SOAP_ITEM);
		
		// execute
		
		CartCart cart = (CartCart) HttpUtil.post(
			"http://localhost:9009/commons/spike/carts", dto, CartCart.class
		); 
		
		// validate
		
		assertNotNull(cart);
		assertEquals(CONSUMER_ID, cart.getConsumerId());
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
		
		assertTrue(cart.getItems().contains(BISCUIT_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_ITEM));
		
		double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
		assertEquals(expectedAmount, cart.computeAmount(), 0.0);
	}
	
	@Test
	@org.junit.jupiter.api.Order(2)
	void getAllCarts() throws IOException {
		
		// execute
		
		CartCart[] carts = (CartCart[]) HttpUtil.get(
			"http://localhost:9009/commons/spike/carts", 
			CartCart[].class
		);
		
		// validate
		
		assertNotNull(carts);
//		assertTrue(carts.length == 0);
		
		assertTrue(carts.length > 0);
		
		CartCart cart = carts[0];
		assertNotNull(cart);
		assertEquals(CONSUMER_ID, cart.getConsumerId());
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
		
		assertTrue(cart.getItems().contains(BISCUIT_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_ITEM));
		
		double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
		assertEquals(expectedAmount, cart.computeAmount(), 0.0);
	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	void deleteCartAndCartItems() throws IOException {
		
		// prepare
		
		CartCart[] carts = (CartCart[]) HttpUtil.get(
			"http://localhost:9009/commons/spike/carts", 
			CartCart[].class
		);
		assertNotNull(carts);
		assertTrue(carts.length == 1);
		Integer cartId = carts[0].getId();
		
		// execute
		
		System.out.println("~~~~~~> " + "http://localhost:9009/commons/spike/carts/" + cartId);
		
		HttpUtil.delete(
			"http://localhost:9009/commons/spike/carts/" + cartId
		);
		
		// validate
		
		carts = (CartCart[]) HttpUtil.get(
			"http://localhost:9009/commons/spike/carts", 
			CartCart[].class
		);
		
		assertNotNull(carts);
		assertTrue(carts.length == 0);
	}
	
}
