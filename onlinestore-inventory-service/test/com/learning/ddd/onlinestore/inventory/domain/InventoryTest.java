package com.learning.ddd.onlinestore.inventory.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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

//What an Inventory can have and should do?
//1: Inventory contains lot of items, basically lot of Products
//2: Mart team can add batches of already supported items
//3: Mart team can add new set of items (if management decided to add new products)
//4: Mart team can remove existing items (say some problem found in some items or remove due to some problem)
//5: Mart team/Management may want to review Inventory (how many items in stock for each supported product?)
//6: Mart team/Management may want to search some/many specific item/product)
//			get a specific item by itemId
//			search item(s) by any combination of parameters of Item

@RunWith(JUnitPlatform.class)
public class InventoryTest {

	final Item BISCUIT_ITEM = new Item(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
	final Item CHIVDA_ITEM = new Item(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
	final Item BATHING_SOAP_ITEM = new Item(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);

	private Inventory inventory;
	private DomainEventsPublisher domainEventPublisher;
	private DomainEventSubscriber domainEventSubscriber;
	private DomainEventService domainEventService;
	
	@BeforeEach
	void setup() {
		
		//setup
		inventory = new Inventory();
		
		domainEventSubscriber = new SampleEventsSubscriber();
		List<DomainEventSubscriber> domainEventSubscribers = new ArrayList<>();
		domainEventSubscribers.add(domainEventSubscriber);
		
		domainEventService = new DomainEventService();
		domainEventService.setDomainEventSubscribers(domainEventSubscribers);
		
		domainEventPublisher = new SampleDomainEventsPublisher();
		domainEventPublisher.setDomainEventService(domainEventService);
		
		inventory.setDomainEventPublisher(domainEventPublisher);

		
		// add multiple items at a go
		inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		// add single item at a go
		inventory.addItem(BATHING_SOAP_ITEM);
	}
	
	@Test
	void getAllItems() {
		List<Item> items = inventory.getItems();
		assertNotNull(items);
		assertFalse(items.isEmpty());
	}

	@Test
	void searchSpecificItem() {
		Item item = inventory.searchItem(BISCUIT_ITEM.getItemId());
		assertNotNull(item);
	}
	
	@Test
	void searchItemsMatchingCategory() {
		Item exampleItem = new Item();
		exampleItem.setCategory("Grocery");
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(2, items.size());
		assertTrue(items.contains(BISCUIT_ITEM));
		assertTrue(items.contains(CHIVDA_ITEM));
		assertFalse(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	void searchItemsMatchingSubCategory() {
		Item exampleItem = new Item();
		exampleItem.setSubCategory("Bathing Soap");
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
	}
	
	@Test
	void searchItemsMatchingName() {
		Item exampleItem = new Item();
		exampleItem.setSubCategory("Bathing Soap");
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
	}
	
	@Test
	void searchItemsMatchingQuantity() {
		Item exampleItem = new Item();
		exampleItem.setQuantity(10);
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(2, items.size());
		assertTrue(items.contains(BISCUIT_ITEM));
		assertTrue(items.contains(CHIVDA_ITEM));
		assertFalse(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	void searchItemsMatchingPrice() {
		Item exampleItem = new Item();
		exampleItem.setPrice(30.0);
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
	}
	
//	@Test
//	void wildcardSearchForItemsWhoCategoryMatchesGivenCharacters() {
//		Item exampleItem = new Item();
//		exampleItem.setCategory("Gro");
//		List<Item> items = inventory.searchItems(exampleItem);
//		assertNotNull(items);
//		assertEquals(2, items.size());
//		assertTrue(items.contains(BISCUIT_ITEM));
//		assertTrue(items.contains(CHIVDA_ITEM));
//	}
	
	@Test
	void removeSpecificItem() {
		boolean isItemRemoved = inventory.removeItem(BISCUIT_ITEM);
		assertTrue(isItemRemoved);
	}

	@Test
	void removeItemsMatchingGivenCriteria() {
		
		// pattern-1
		Item exampleItem = new Item();
		exampleItem.setCategory("Grocery");
		inventory.removeItems(exampleItem);
		assertTrue(inventory.searchItems(exampleItem).isEmpty());
		
		// pattern-2
		exampleItem = new Item();
		exampleItem.setCategory("Toiletries");
		inventory.removeItems(exampleItem);
		assertTrue(inventory.searchItems(exampleItem).isEmpty());
		
		// pattern-3
		exampleItem = new Item();
		exampleItem.setName("Parle-G");
		inventory.removeItems(exampleItem);
		assertTrue(inventory.searchItems(exampleItem).isEmpty());
	}
	
}
