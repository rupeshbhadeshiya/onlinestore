package com.learning.ddd.onlinestore.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventSubscriber;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.DummyDomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.DummyDomainEventsSubscriber;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.repository.InventoryItemRepository;

//What an Inventory can have and should do?
//1: Inventory contains lot of items, basically lot of Products
//2: Mart team can add batches of already supported items
//3: Mart team can add new set of items (if management decided to add new products)
//4: Mart team can remove existing items (say some problem found in some items or remove due to some problem)
//5: Mart team/Management may want to review Inventory (how many items in stock for each supported product?)
//6: Mart team/Management may want to search some/many specific item/product)
//			get a specific item by itemId
//			search item(s) by any combination of parameters of Item

// Test created for just testing code without any DB/other system inclusion, just hard-coded values in code
//@RunWith(JUnitPlatform.class)
//@RunWith(SpringRunner.class)
public class InventoryTest {

	final InventoryItem BISCUIT_ITEM = new InventoryItem(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
	final InventoryItem CHIVDA_ITEM = new InventoryItem(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
	final InventoryItem BATHING_SOAP_ITEM = new InventoryItem(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);

	private Inventory inventory;
	
	@Mock
	private InventoryItemRepository itemRepository;
	
	@Mock
	private DomainEventsPublisher domainEventPublisher;
	
	@Mock
	private DomainEventSubscriber domainEventSubscriber;
	
//	@Mock
//	private DomainEventService domainEventService;
	
	@BeforeEach
	void setup() {
		
		//setup
		inventory = new Inventory();
		
		inventory.setItemRepository(itemRepository);
		
		domainEventSubscriber = new DummyDomainEventsSubscriber();
		List<DomainEventSubscriber> domainEventSubscribers = new ArrayList<>();
		domainEventSubscribers.add(domainEventSubscriber);
		
//		domainEventService = new DomainEventService();
//		domainEventService.setDomainEventSubscribers(domainEventSubscribers);
		
		domainEventPublisher = new DummyDomainEventsPublisher();
		//domainEventPublisher.setDomainEventService(domainEventService);
		inventory.setDomainEventPublisher(domainEventPublisher);
		
		// add multiple items at a go
		inventory.addItems( Arrays.asList( new InventoryItem[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		// add single item at a go
		inventory.addItem(BATHING_SOAP_ITEM);
	}
	
	@Test
	//@org.junit.Test
	void getAllItems() {
		List<InventoryItem> items = inventory.getItems();
		assertNotNull(items);
		assertFalse(items.isEmpty());
	}

	@Test
	void searchSpecificItem() {
		InventoryItem item = inventory.getItem(BISCUIT_ITEM.getItemId());
		assertNotNull(item);
	}
	
	@Test
	void searchItemsMatchingCategory() {
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setCategory("Grocery");
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(2, items.size());
		assertTrue(items.contains(BISCUIT_ITEM));
		assertTrue(items.contains(CHIVDA_ITEM));
		assertFalse(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	void searchItemsMatchingSubCategory() {
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setSubCategory("Bathing Soap");
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
	}
	
	@Test
	void searchItemsMatchingName() {
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setSubCategory("Bathing Soap");
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
	}
	
	@Test
	void searchItemsMatchingQuantity() {
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setQuantity(10);
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(2, items.size());
		assertTrue(items.contains(BISCUIT_ITEM));
		assertTrue(items.contains(CHIVDA_ITEM));
		assertFalse(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	void searchItemsMatchingPrice() {
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setPrice(30.0);
		List<InventoryItem> items = inventory.searchItems(exampleItem);
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
//		boolean isItemRemoved = inventory.removeItem(BISCUIT_ITEM);
//		assertTrue(isItemRemoved);
	}

	@Test
	void removeItemsMatchingGivenCriteria() {
		
		// pattern-1
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setCategory("Grocery");
		inventory.removeItems(exampleItem);
		assertTrue(inventory.searchItems(exampleItem).isEmpty());
		
		// pattern-2
		exampleItem = new InventoryItem();
		exampleItem.setCategory("Toiletries");
		inventory.removeItems(exampleItem);
		assertTrue(inventory.searchItems(exampleItem).isEmpty());
		
		// pattern-3
		exampleItem = new InventoryItem();
		exampleItem.setName("Parle-G");
		inventory.removeItems(exampleItem);
		assertTrue(inventory.searchItems(exampleItem).isEmpty());
	}
	
}
