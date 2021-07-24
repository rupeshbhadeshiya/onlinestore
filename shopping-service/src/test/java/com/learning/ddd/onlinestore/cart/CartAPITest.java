package com.learning.ddd.onlinestore.cart;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.commons.util.HttpUtil;

//~Cart-Specific~
//
//Consumer pulls a Cart ; So, a Cart is Consumer specific; Consumer:Cart = 1:*
//Add items to a Cart ; So, effectively a Cart is an item-holder from Inventory to Checkout
//Get all Carts for a Consumer
//View items in a Cart
//Remove items from a Cart
//
//~End~

@TestMethodOrder(OrderAnnotation.class)
class CartAPITest {

	private static final String CONSUMER_ID = "11";
	private static final String CART_SERVICE_URL = 
		"http://localhost:9020/consumers/"+CONSUMER_ID+"/carts";
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final CartItem BISCUIT_ITEM = new CartItem(
		"Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final CartItem BATHING_SOAP_ITEM = new CartItem(
		"Toiletries", "Bathing Soap", "Mysore Sandal Soap", 
		BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	private static int CART_ID;
	
	
	@Test
	@org.junit.jupiter.api.Order(1)
	void pullCartAndAddItems() throws IOException {
		
		// prepare
		
		PullCartDTO dto = new PullCartDTO(CONSUMER_ID);
		dto.addItem(BISCUIT_ITEM);
		dto.addItem(BATHING_SOAP_ITEM);
		
		// execute
		
		Cart cart = (Cart) HttpUtil.post(CART_SERVICE_URL, dto, Cart.class); 
		
		// record for use in other tests
		CART_ID = cart.getId();
		
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
		
		Cart[] carts = (Cart[]) HttpUtil.get(CART_SERVICE_URL, Cart[].class);
		
		// validate
		
		assertNotNull(carts);
		assertTrue(carts.length > 0);
		
		Cart cart = carts[0];
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
	void getOneCartAndItsCartItems() throws IOException {
		
		// execute

		Cart cart = (Cart) HttpUtil.get(
			CART_SERVICE_URL + "/" + CART_ID, 
			Cart.class
		);
		
		// validate
		
		assertNotNull(cart);
		assertEquals(CONSUMER_ID, cart.getConsumerId());
		assertEquals(CART_ID, cart.getId());
		
		assertNotNull(cart.getItems());
		final int expectedQuantity = BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY;
		assertEquals(expectedQuantity, cart.getItemCount());
		
		assertTrue(cart.getItems().contains(BISCUIT_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_ITEM));
		
		final double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
		assertEquals(expectedAmount, cart.computeAmount(), 0.0);
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	void removeOneItemFromCartAndVerifyInReturnedCartObject() 
			throws CartNotFoundException, CartItemNotFoundException, IOException {

		// prepare
		
		Cart cart = (Cart) HttpUtil.get(
			CART_SERVICE_URL + "/" + CART_ID, 
			Cart.class
		);
		assertNotNull(cart);
		System.out.println("removeOneItemFromCart(): cart="+cart);
		
		// execute
		
		CartItem[] itemsToRemove = new CartItem[] { BATHING_SOAP_ITEM };
		
		HttpUtil.delete(
			CART_SERVICE_URL + "/" + CART_ID + "/items",
			Arrays.asList(itemsToRemove)
		);
		
		//Cart updatedCart = cartService.removeItems(CART_ID, itemsToRemove);
		//System.out.println("removeOneItemFromCart(): updatedCart="+updatedCart);
		
		// validate
		
		Cart updatedCart = (Cart) HttpUtil.get(
			CART_SERVICE_URL + "/" + CART_ID, 
			Cart.class
		);
		assertNotNull(updatedCart);
		assertFalse(updatedCart.getItems().contains(BATHING_SOAP_ITEM));
		assertTrue(updatedCart.getItems().contains(BISCUIT_ITEM));
	}
	
	@Test
	@org.junit.jupiter.api.Order(5)
	void releaseCartAndRemoveItems() throws IOException {
		
		// execute

		HttpUtil.delete(CART_SERVICE_URL + "/" + CART_ID);
		
		// validate
		
		Cart deletedCart = (Cart) HttpUtil.get(
			CART_SERVICE_URL + "/" + CART_ID, 
			Cart.class
		);
		
		assertNull(deletedCart);
		
	}
}
