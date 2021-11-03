package com.learning.ddd.onlinestore.cart;

// Test created for just testing code without any DB/other system inclusion, just hard-coded values in code
//@RunWith(JUnitPlatform.class)
public class CartTest {

//	private static final int BISCUIT_ITEM_ID = 1001;
//	private static final int BATHING_SOAP_ITEM_ID = 2002;
//	private static final int BISCUIT_ITEM_QUANTITY = 2;
//	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
//	private final Item BISCUIT_ITEM = new Item(BISCUIT_ITEM_ID, "Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
//	private final Item BATHING_SOAP_ITEM = new Item(BATHING_SOAP_ITEM_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
//	
//	private Cart cart;
//	private DomainEventsPublisher domainEventPublisher;
//	private DomainEventSubscriber domainEventSubscriber;
//	private DomainEventService domainEventService;
//	
//	@BeforeEach
//	void setup() {
//		
//		cart = new Cart();
//				
//		domainEventSubscriber = new DummyDomainEventsSubscriber();
//		List<DomainEventSubscriber> domainEventSubscribers = new ArrayList<>();
//		domainEventSubscribers.add(domainEventSubscriber);
//		
//		domainEventService = new DomainEventService();
//		domainEventService.setDomainEventSubscribers(domainEventSubscribers);
//		
//		domainEventPublisher = new DummyDomainEventsPublisher();
//		domainEventPublisher.setDomainEventService(domainEventService);
//		
//		cart.setDomainEventPublisher(domainEventPublisher);
//	}
//	
//	@Test
//	void addItemsToShoppingCartAndReview() {
//		
//		// add items
//		cart.addOrUpdateItem(BISCUIT_ITEM);
//		cart.addOrUpdateItem(BATHING_SOAP_ITEM);
//		
//		// verify
//		assertNotNull(cart.getItems());
//		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
//		
//		Item biscuitItem = cart.getItem(BISCUIT_ITEM_ID);
//		assertEquals(BISCUIT_ITEM, biscuitItem);
//		
//		Item bathingSoapItem = cart.getItem(BATHING_SOAP_ITEM_ID);
//		assertEquals(BATHING_SOAP_ITEM, bathingSoapItem);
//	}
//	
//	@Test
//	void removeItemsFromShoppingCartAndReview() {
//		
//		cart.addOrUpdateItem(BISCUIT_ITEM);
//		cart.addOrUpdateItem(BATHING_SOAP_ITEM);
//		
//		assertNotNull(cart.getItems());
//		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
//		
//		// remove items
//		cart.removeItem(BATHING_SOAP_ITEM);
//		
//		// verify overall quantity is decreased
//		
//		assertNotNull(cart.getItems());
//		assertEquals(BISCUIT_ITEM_QUANTITY, cart.getItemCount());
//		
//		// verify Biscuits are not removed
//		Item biscuitItem = cart.getItem(BISCUIT_ITEM_ID);
//		assertEquals(BISCUIT_ITEM, biscuitItem);
//		
//		// verify Bathing Soap is removed
//		assertNull(cart.getItem(BATHING_SOAP_ITEM_ID));
//	}
//	
//	@Test
//	void increaseItemQuantitiesInShoppingCartAndReview() {
//		
//		cart.addOrUpdateItem(BISCUIT_ITEM);
//		cart.addOrUpdateItem(BATHING_SOAP_ITEM);
//		
//		assertNotNull(cart.getItems());
//		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
//		
//		// increase Bathing Soap quantity
//		cart.increaseItemQuantity(BATHING_SOAP_ITEM_ID, 2);
//		
//		assertNotNull(cart.getItems());
//		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY + 2, cart.getItemCount());
//		
//		// verify Bathing Soap quantity increased and Biscuit quantity remained unchanged
//		Item biscuitItem = cart.getItem(BATHING_SOAP_ITEM_ID);
//		assertEquals(BATHING_SOAP_ITEM, biscuitItem);
//		assertEquals(BATHING_SOAP_ITEM_QUANTITY + 2, biscuitItem.getQuantity());
//	}
//	
//	@Test
//	void decreaseItemQuantitiesInShoppingCartAndReview() {
//		
//		cart.addOrUpdateItem(BISCUIT_ITEM);
//		cart.addOrUpdateItem(BATHING_SOAP_ITEM);
//		
//		assertNotNull(cart.getItems());
//		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
//		
//		// decrease quantity
//		cart.decreaseItemQuantity(BISCUIT_ITEM_ID, 1);
//		
//		assertNotNull(cart.getItems());
//		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY - 1, cart.getItemCount());
//		
//		// verify Biscuit quantity decreased and Bathing Soap quantity remained unchanged
//		Item biscuitItem = cart.getItem(BISCUIT_ITEM_ID);
//		assertEquals(BISCUIT_ITEM, biscuitItem);
//		assertEquals(BISCUIT_ITEM_QUANTITY - 1, biscuitItem.getQuantity());
//	}
//	
//	@Test
//	void viewAllItemsInShoppingCart() {
//		
//		cart.addOrUpdateItem(BISCUIT_ITEM);
//		cart.addOrUpdateItem(BATHING_SOAP_ITEM);
//		
//		assertNotNull(cart.getItems());
//		assertEquals(2, cart.getItems().size());
//		//assertEquals(BISCUIT_ITEM, cart.getI);
//		assertTrue(cart.getItems().contains(BISCUIT_ITEM));
//		assertTrue(cart.getItems().contains(BATHING_SOAP_ITEM));
//		
//	}
	
}
