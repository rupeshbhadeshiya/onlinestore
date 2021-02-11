package com.learning.ddd.onlinestore.shopping.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventService;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventSubscriber;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.SampleDomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.SampleEventsSubscriber;
import com.learning.ddd.onlinestore.inventory.domain.Item;

@RunWith(JUnitPlatform.class)
public class ShoppingCartTest {

	private static final int BISCUIT_ITEM_ID = 1001;
	private static final int BATHING_SOAP_ITEM_ID = 2002;
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final Item BISCUIT_ITEM = new Item(BISCUIT_ITEM_ID, "Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final Item BATHING_SOAP_ITEM = new Item(BATHING_SOAP_ITEM_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	private ShoppingCart cart;
	private DomainEventsPublisher domainEventPublisher;
	private DomainEventSubscriber domainEventSubscriber;
	private DomainEventService domainEventService;
	
	@BeforeEach
	void setup() {
		
		cart = new ShoppingCart();
				
		domainEventSubscriber = new SampleEventsSubscriber();
		List<DomainEventSubscriber> domainEventSubscribers = new ArrayList<>();
		domainEventSubscribers.add(domainEventSubscriber);
		
		domainEventService = new DomainEventService();
		domainEventService.setDomainEventSubscribers(domainEventSubscribers);
		
		domainEventPublisher = new SampleDomainEventsPublisher();
		domainEventPublisher.setDomainEventService(domainEventService);
		
		cart.setDomainEventPublisher(domainEventPublisher);
	}
	
	@Test
	void addItemsToShoppingCartAndReview() {
		
		// add items
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		
		// verify
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
		
		Item biscuitItem = cart.getItem(BISCUIT_ITEM_ID);
		assertEquals(BISCUIT_ITEM, biscuitItem);
		
		Item bathingSoapItem = cart.getItem(BATHING_SOAP_ITEM_ID);
		assertEquals(BATHING_SOAP_ITEM, bathingSoapItem);
	}
	
	@Test
	void removeItemsFromShoppingCartAndReview() {
		
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
		
		// remove items
		cart.removeItem(BATHING_SOAP_ITEM);
		
		// verify overall quantity is decreased
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY, cart.getItemCount());
		
		// verify Biscuits are not removed
		Item biscuitItem = cart.getItem(BISCUIT_ITEM_ID);
		assertEquals(BISCUIT_ITEM, biscuitItem);
		
		// verify Bathing Soap is removed
		assertNull(cart.getItem(BATHING_SOAP_ITEM_ID));
	}
	
	@Test
	void increaseItemQuantitiesInShoppingCartAndReview() {
		
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
		
		// increase Bathing Soap quantity
		cart.increaseItemQuantity(BATHING_SOAP_ITEM_ID, 2);
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY + 2, cart.getItemCount());
		
		// verify Bathing Soap quantity increased and Biscuit quantity remained unchanged
		Item biscuitItem = cart.getItem(BATHING_SOAP_ITEM_ID);
		assertEquals(BATHING_SOAP_ITEM, biscuitItem);
		assertEquals(BATHING_SOAP_ITEM_QUANTITY + 2, biscuitItem.getQuantity());
	}
	
	@Test
	void decreaseItemQuantitiesInShoppingCartAndReview() {
		
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, cart.getItemCount());
		
		// decrease quantity
		cart.decreaseItemQuantity(BISCUIT_ITEM_ID, 1);
		
		assertNotNull(cart.getItems());
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY - 1, cart.getItemCount());
		
		// verify Biscuit quantity decreased and Bathing Soap quantity remained unchanged
		Item biscuitItem = cart.getItem(BISCUIT_ITEM_ID);
		assertEquals(BISCUIT_ITEM, biscuitItem);
		assertEquals(BISCUIT_ITEM_QUANTITY - 1, biscuitItem.getQuantity());
	}
	
	@Test
	void viewAllItemsInShoppingCart() {
		
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		
		assertNotNull(cart.getItems());
		assertEquals(2, cart.getItems().size());
		//assertEquals(BISCUIT_ITEM, cart.getI);
		assertTrue(cart.getItems().contains(BISCUIT_ITEM));
		assertTrue(cart.getItems().contains(BATHING_SOAP_ITEM));
		
	}
	
}
