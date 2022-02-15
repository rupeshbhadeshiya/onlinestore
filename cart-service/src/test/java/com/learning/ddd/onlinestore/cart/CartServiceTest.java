package com.learning.ddd.onlinestore.cart;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learning.ddd.onlinestore.cart.application.dto.AddItemToCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

//~Cart-Specific~
//
// Consumer pulls a Cart ; So, a Cart is Consumer specific; Consumer:Cart = 1:*
// Add items to a Cart ; So, effectively a Cart is an item-holder from Inventory to Checkout
// Get all Carts for a Consumer
// View items in a Cart
// Remove items from a Cart
//
//~End~

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class CartServiceTest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final CartItem BISCUIT_ITEM = new CartItem("Grocery", "Biscuit", "Parle-G", 10.0, BISCUIT_ITEM_QUANTITY);
	private final CartItem BATHING_SOAP_ITEM = new CartItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, BATHING_SOAP_ITEM_QUANTITY);
	
	private static int CART_ID;
	
	@Autowired
	private CartService cartService;
	
	@Test
	@org.junit.jupiter.api.Order(1)
	void addItemsInCart() throws JMSException {
		
		AddItemToCartDTO dto = new AddItemToCartDTO(CONSUMER_ID, 0, BISCUIT_ITEM);
		Cart cart = cartService.addItem(dto);
		
		dto = new AddItemToCartDTO(CONSUMER_ID, 0, BATHING_SOAP_ITEM);
		cart = cartService.addItem(dto);
		
		// record for use in other tests
		CART_ID = cart.getCartId();
		
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
	@org.junit.jupiter.api.Order(2)
	void getAllCartsAndTheirCartItems() {
		
		List<Cart> carts = cartService.getAllCarts(CONSUMER_ID);
		
		assertNotNull(carts);
		assertTrue(carts.size() > 0);
		
		Cart cart = carts.get(0);
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
	void getOneCartAndItsCartItems() throws CartNotFoundException {
		
		Cart cart = cartService.getCart(CART_ID);
		
		assertNotNull(cart);
		assertEquals(CONSUMER_ID, cart.getConsumerId());
		assertEquals(CART_ID, cart.getCartId());
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
		
		assertTrue(cart.getItems().contains(BISCUIT_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_ITEM));
		
		double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
		assertEquals(expectedAmount, cart.computeAmount(), 0.0);
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	void removeOneItemFromCartAndVerifyInReturnedCartObject() throws CartNotFoundException, CartItemNotFoundException {

		// prepare
		
		Cart cart = cartService.getCart(CART_ID);
		assertNotNull(cart);
		System.out.println("removeOneItemFromCart(): cart="+cart);
		
		// execute
		
		CartItem[] itemsToRemove = new CartItem[] { BATHING_SOAP_ITEM };
		
		cartService.removeItems(CART_ID, Arrays.asList(itemsToRemove));
		
		//Cart updatedCart = cartService.removeItems(CART_ID, itemsToRemove);
		//System.out.println("removeOneItemFromCart(): updatedCart="+updatedCart);
		
		// validate
		Cart updatedCart = cartService.getCart(CART_ID);
		assertNotNull(updatedCart);
		assertFalse(updatedCart.getItems().contains(BATHING_SOAP_ITEM));
		assertTrue(updatedCart.getItems().contains(BISCUIT_ITEM));
	}
	
	@Test
	@org.junit.jupiter.api.Order(5)
	void verifyRemovedItemOnceMoreByLoadingUpdatedCartFromDatabase() throws CartNotFoundException, CartItemNotFoundException {

		// validate
		
		// also validate other way
		Cart updatedCart = cartService.getCart(CART_ID);
		
		assertNotNull(updatedCart);
		assertFalse(updatedCart.getItems().contains(BATHING_SOAP_ITEM));
		assertTrue(updatedCart.getItems().contains(BISCUIT_ITEM));
	}
	
	@Test
	@org.junit.jupiter.api.Order(6)
	void catchCartNotFoundExceptionInRemoveItems() {

		// prepare
		
		int notPresentCartId = -1;
		CartItem[] anyItems = new CartItem[] { new CartItem() };
		
		// execute
		
		CartNotFoundException ex = assertThrows(
			CartNotFoundException.class, () -> {
				cartService.removeItems(notPresentCartId, Arrays.asList(anyItems));
			}
		);
		
		// validate
		
		assertNotNull(ex);
		assertEquals(notPresentCartId, ex.getCartId());
	}
	
	@Test
	@org.junit.jupiter.api.Order(6)
	void catchCartItemNotFoundExceptionInRemoveItems() {

		// prepare
		
		CartItem[] notPresentItems = new CartItem[] { new CartItem() };
		
		// execute
		
		CartItemNotFoundException ex = assertThrows(
			CartItemNotFoundException.class, () -> {
				cartService.removeItems(CART_ID, Arrays.asList(notPresentItems));
			}
		);
		
		// validate
		
		assertNotNull(ex);
		assertEquals(CART_ID, ex.getCart().getCartId());
		assertEquals(notPresentItems[0], ex.getCartItem());
	}
	
	
	@Test
	@org.junit.jupiter.api.Order(33) // too big number to make sure it executes last!
	void emptyCart() throws CartNotFoundException, CloneNotSupportedException, JMSException {
		
		cartService.emptyCart(CART_ID, DomainEventName.CART_EMPTIED_BY_CONSUMER);
		
		assertNull(cartService.getCart(CART_ID));
	}
	
	@Test
	@org.junit.jupiter.api.Order(34)
	void catchCartItemNotFoundExceptionInEmptyCart() {

		// prepare
		
		final int unknownCartId = -1;
		
		// execute
		
		CartItemNotFoundException ex = assertThrows(
			CartItemNotFoundException.class, () -> {
				cartService.emptyCart(unknownCartId, DomainEventName.CART_EMPTIED_BY_CONSUMER);
			}
		);
		
		// validate
		
		assertNotNull(ex);
		assertEquals(unknownCartId, ex.getCart().getCartId());
	}
	
}
