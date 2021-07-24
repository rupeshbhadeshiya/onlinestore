package com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class CartCartServiceTest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final CartCartItem BISCUIT_ITEM = new CartCartItem("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final CartCartItem BATHING_SOAP_ITEM = new CartCartItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	@Autowired
	private CartCartService cartService;
	
	@Test
	@Order(1)
	void pullCartAndAddItems() {
		
		PullCartCartDTO dto = new PullCartCartDTO(CONSUMER_ID);
		dto.addItem(BISCUIT_ITEM);
		dto.addItem(BATHING_SOAP_ITEM);
		
		CartCart cart = cartService.pullCartAndAddItems(dto);
		
		assertNotNull(cart);
		assertEquals(CONSUMER_ID, cart.getConsumerId());
		
		assertNotNull(cart.getItems());
		final int expectedQuantity = BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY;
		assertEquals(expectedQuantity, cart.getItemCount());
		
		assertTrue(cart.getItems().contains(BISCUIT_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_ITEM));
		
		final double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
		assertEquals(expectedAmount, cart.computeAmount(), 0.0);
	}
	
	@Test
	@Order(2)
	void getAllCartsAndTheirCartItems() {
		
		List<CartCart> carts = cartService.getAllCarts();
		
		assertNotNull(carts);
//		assertTrue(carts.length == 0);
		
		assertTrue(carts.size() > 0);
		
		CartCart cart = carts.get(0);
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
	@Order(3)
	void deleteCartAndCartItems() {
		
		CartCart cart = cartService.getAllCarts().get(0);
		
		cartService.deleteCart(cart);
		
		List<CartCart> carts = cartService.getAllCarts();
		assertNotNull(carts);
		assertEquals(0, carts.size());
	}
	
}
