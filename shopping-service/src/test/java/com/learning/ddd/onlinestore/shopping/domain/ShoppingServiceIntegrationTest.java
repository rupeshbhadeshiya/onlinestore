package com.learning.ddd.onlinestore.shopping.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learning.ddd.onlinestore.shopping.domain.repository.CartRepository;
import com.learning.ddd.onlinestore.shopping.domain.service.CartService;

// Cart holds Items till they become Order/Shipment

//~Shopping-Specific~
// Consumer pulls a Cart ; So, a Cart is Consumer specific; Consumer:Cart = 1:*
// Add items to a Cart ; So, effectively a Cart is an item-holder from Inventory to Checkout
// Get all Carts for a Consumer
// View items in a Cart
// Remove items from a Cart 
//~End~

//~Overall~
// Mart has an Inventory (Storage space)
// Mart team adds Items / Products to the Inventory
// Mart team arranges some Items in Shopping Racks
// Consumer enters Mart and pulls a Cart
// Consumer shops Items from Shopping Racks to the Cart
// Consumer visits a Checkout counter
// Consumer hands over Items from the Cart to Checkout team
// Checkout team collects few details from Consumer: Payment Method, Billing Address, Shipping Address, Contact No/Email/Name
// Checkout team manages payment
// Checkout team hands over Payment Receipt and Items to Consumer
// Consumer exits Mart with Payment Receipt and Items
//~End~

@SpringBootTest
public class ShoppingServiceIntegrationTest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_ID = 1001;
	private static final int BATHING_SOAP_ITEM_ID = 2002;
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final Item BISCUIT_ITEM = new Item(BISCUIT_ITEM_ID, "Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final Item BATHING_SOAP_ITEM = new Item(BATHING_SOAP_ITEM_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepository;

	//
	// get cart - empty cart with no items
	// save cart - cart and items all are saved
	// update cart - cart and items all are update
	// delete cart - cart and items all are deleted
	// cart:items = 1:*
	//
	
	@BeforeEach
	void beforeEachTestSetup() {
		// empty for now!
	}
	
	@AfterEach
	void cleanupForEachTest() {
		cartRepository.deleteAll(); // necessary to ensure each test starts a clean
	}
	
	@Test
	void addItemsToCartAndReview() {
		
		Cart cart = new Cart(CONSUMER_ID);
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		cartService.saveCart(cart);
		
		Cart cart1FromDB = cartService.getCart(cart.getCartId());
		assertNotNull(cart1FromDB);
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart1FromDB.getItemCount());
	}
	
	@Test
	void getAllCartsOfConsumer() {
		
		// initially no Cart!
		assertTrue(cartService.getCarts(CONSUMER_ID).isEmpty());
		
		// later, as many Carts as being used!
		Cart cart = new Cart(CONSUMER_ID);
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		cartService.saveCart(cart);
		
		List<Cart> carts = cartService.getCarts(CONSUMER_ID);
		
		assertNotNull(carts);
		assertEquals(1, carts.size());
		Cart cart2FromDB = carts.get(0);
		assertNotNull(cart2FromDB);
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart2FromDB.getItemCount());
	}
	
	@Test
	void removeItemsFromCart() {
		
		Cart cart = new Cart(CONSUMER_ID);
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		cartService.saveCart(cart);
		
		Cart cartFromDB = cartService.getCart(cart.getCartId());
		System.out.println("\n~~~~~~ removeItemsFromCart(): Before JPA Removal: Cart=" + cartFromDB + "\n");
		final int CART_ID = cartFromDB.getCartId();
		assertNotNull(cartFromDB);
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cartFromDB.getItemCount());
		
		cartService.removeItemFromCart(cartFromDB, BATHING_SOAP_ITEM);
		
		cartFromDB = cartService.getCart(CART_ID);
		System.out.println("\n~~~~~~ removeItemsFromCart(): After JPA Removal: Cart=" + cartFromDB + "\n");
		assertNotNull(cartFromDB);
		assertEquals(BISCUIT_ITEM_QUANTITY, cartFromDB.getItemCount());
		assertTrue(BISCUIT_ITEM.equals(cartFromDB.getItems().get(0)));
		assertFalse(cartFromDB.getItems().contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	void deleteCart() {
		
		Cart cart = new Cart(CONSUMER_ID);
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		cartService.saveCart(cart);
		
		cartService.deleteCart(cart);
		
		assertTrue(cartService.getCarts(CONSUMER_ID).isEmpty());
	}

}
