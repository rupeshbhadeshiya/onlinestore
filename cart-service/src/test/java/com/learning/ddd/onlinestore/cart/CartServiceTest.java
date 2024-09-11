package com.learning.ddd.onlinestore.cart;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.jms.JMSException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.learning.ddd.onlinestore.cart.application.dto.AddProductToCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.repository.CartRepository;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.product.domain.Product;

//~Cart-Specific~
//
// Consumer pulls a Cart ; So, a Cart is Consumer specific; Consumer:Cart = 1:*
// Add items to a Cart ; So, effectively a Cart is an item-holder from Inventory to Checkout
// Get all Carts for a Consumer
// View items in a Cart
// Remove items from a Cart
//
//~End~

@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest // this annotation includes @RunWith(SpringRunner.class)
class CartServiceTest {

	private static final String CONSUMER_ID = "11";

	private static final int BISCUIT_CART_ITEM_ID = 11;
	private static final int BATHING_SOAP_CART_ITEM_ID = 22;

	private static final int BISCUIT_CART_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_CART_ITEM_QUANTITY = 3;
	
	private final Product BISCUIT_PRODUCT = new Product(BISCUIT_CART_ITEM_ID, "Grocery", "Biscuit", "Parle-G", 10.0, BISCUIT_CART_ITEM_QUANTITY);
	private final Product BATHING_SOAP_PRODUCT = new Product(BATHING_SOAP_CART_ITEM_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, BATHING_SOAP_CART_ITEM_QUANTITY);
	
	private final CartItem BISCUIT_CART_ITEM = new CartItem(BISCUIT_PRODUCT);
	private final CartItem BATHING_SOAP_CART_ITEM = new CartItem(BATHING_SOAP_PRODUCT);
	
	private static int CART_ID;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepository;
	
	
//	@BeforeEach
//	void setupBeforeEachTest() {
//		//productCatalogService.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
////		BISCUIT_ITEM = productCatalogService.addProduct( BISCUIT_ITEM );
////		CHIVDA_ITEM = productCatalogService.addProduct( CHIVDA_ITEM );
////		BATHING_SOAP_ITEM = productCatalogService.addProduct( BATHING_SOAP_ITEM );
//	}
	
	@AfterEach
	void cleanUpAfterEachTest() {
		// ensures to clean database after each test 
		// so that new test starts with clean database
		cartRepository.deleteAll(); 
	}
	
	
	@Test
	@org.junit.jupiter.api.Order(1)
	void addItemsInCart() throws JMSException {
		
		AddProductToCartDTO dto = new AddProductToCartDTO(CONSUMER_ID, 0, BISCUIT_PRODUCT);
		Cart cart = cartService.addProduct(dto);
		
		dto = new AddProductToCartDTO(CONSUMER_ID, 0, BATHING_SOAP_PRODUCT);
		cart = cartService.addProduct(dto);
		
		// record for use in other tests
		CART_ID = cart.getCartId();
		
		assertNotNull(cart);
		assertEquals(CONSUMER_ID, cart.getConsumerId());
		
		assertNotNull(cart.getItems());
		final int expectedQuantity = BISCUIT_CART_ITEM_QUANTITY + BATHING_SOAP_CART_ITEM_QUANTITY;
		assertEquals(expectedQuantity, cart.getItemCount());
		
		assertTrue(cart.getItems().contains(BISCUIT_CART_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_CART_ITEM));
		
		final double expectedAmount = BISCUIT_CART_ITEM.computeAmount() + BATHING_SOAP_CART_ITEM.computeAmount();
		assertEquals(expectedAmount, cart.computeAmount(), 0.0);
	}
	
	// ...
		// Add Item to Inventory: 	Item in Inventory => (available = true)
		// ...
		// Add Item to Cart:		Item in Inventory => (available = false)
			// Try adding same Item to Cart: --shouldThrow--ItemNotAvailableForShoppingException
		// Remove Item from Cart:	Item in Inventory => (available = true)
			// Try emptying a Cart again and again: --shouldThrow--CartNotFoundException
		// ...
		// Place Order:				Item in Inventory --stays-- (available = false)
		// Cancel Order:			Item in Inventory => (available = true)
			// Try canceling an Order again: --shouldThrow--OrderAlreadyCancelledException
		// ...
	
//	@Test
//	@org.junit.jupiter.api.Order(2)
//	void addingSameItemInCartShouldThrowItemNotAvailableException() throws JMSException {
//		
//		AddItemToCartDTO dto = new AddItemToCartDTO(CONSUMER_ID, 0, BISCUIT_CART_ITEM);
//		cartService.addItem(dto);
//		
//		ItemNotAvailableForShoppingException ex = assertThrows(
//				ItemNotAvailableForShoppingException.class, () -> {
//				cartService.addItem(dto);
//			}
//		);
//		
//		// validate
//		
//		assertNotNull(ex);
//	}
	
	@Test
	@org.junit.jupiter.api.Order(2)
	void getAllCartsAndTheirCartItems() throws JMSException {
		
		// first check that there are no Carts
		
		List<Cart> carts = cartService.getAllCarts(CONSUMER_ID);
		
		assertNotNull(carts);
		assertEquals(0, carts.size());
		
		// now add a Cart and check that there are Carts in local data store
		
		AddProductToCartDTO dto = new AddProductToCartDTO(CONSUMER_ID, 0, BISCUIT_PRODUCT);
		Cart cart = cartService.addProduct(dto);
		
		dto = new AddProductToCartDTO(CONSUMER_ID, 0, BATHING_SOAP_PRODUCT);
		cart = cartService.addProduct(dto);
		
		carts = cartService.getAllCarts(CONSUMER_ID);
		
		assertNotNull(carts);
		assertTrue(carts.size() > 0);
		
		cart = carts.get(0);
		assertNotNull(cart);
		assertEquals(CONSUMER_ID, cart.getConsumerId());
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_CART_ITEM_QUANTITY + BATHING_SOAP_CART_ITEM_QUANTITY, cart.getItemCount());
		
		assertTrue(cart.getItems().contains(BISCUIT_CART_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_CART_ITEM));
		
		double expectedAmount = BISCUIT_CART_ITEM.computeAmount() + BATHING_SOAP_CART_ITEM.computeAmount();
		assertEquals(expectedAmount, cart.computeAmount(), 0.0);
	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	void withNoCartsInDatabaseTryCatchingCartNotFoundException() throws CartNotFoundException {
		
		// set up
		// nothing to set
		
		// test
		
		CartNotFoundException ex = assertThrows(
				CartNotFoundException.class, () -> {
				cartService.getCart(CART_ID);
			}
		);
		
		// validate
		
		assertNotNull(ex);
	}
	
//	@Test
//	@org.junit.jupiter.api.Order(5)
//	void verifyRemovedItemOnceMoreByLoadingUpdatedCartFromDatabase() throws CartNotFoundException, CartItemNotFoundException {
//
//		// validate
//		
//		// also validate other way
//		Cart updatedCart = cartService.getCart(CART_ID);
//		
//		assertNotNull(updatedCart);
//		assertFalse(updatedCart.getItems().contains(BATHING_SOAP_CART_ITEM));
//		assertTrue(updatedCart.getItems().contains(BISCUIT_CART_ITEM));
//	}
//	
//	
//	@Test
//	@org.junit.jupiter.api.Order(33) // too big number to make sure it executes last!
//	void emptyCartAndExpectCartNotFoundException() throws CartNotFoundException, CloneNotSupportedException, JMSException {
//		
//		cartService.emptyCart(CART_ID, OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER);
//		
//		Throwable throwable =  assertThrows(CartNotFoundException.class, () -> {
//			cartService.getCart(CART_ID);
//		});
//		assertEquals(CartNotFoundException.class, throwable.getClass());
//		
//	}
	
}
