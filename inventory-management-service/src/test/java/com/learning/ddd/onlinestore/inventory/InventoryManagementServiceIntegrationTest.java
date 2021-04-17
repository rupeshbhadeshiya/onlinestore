package com.learning.ddd.onlinestore.inventory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.Item;
import com.learning.ddd.onlinestore.inventory.domain.repository.ItemRepository;

// An Inventory contains Items; it may be referred as Item Store. So you don't need to create an Inventory.

//~Inventory-Specific~
// Add items to Inventory
// Get all items from Inventory
// Retrieve a specific item  from Inventory by its unique identifier
// Search items in Inventory through any combination of Item attributes
// Remove a specific item from Inventory
// Remove items from Inventory through any combination of attributes
//~End~

//~Overall~
//Mart has an Inventory (Storage space)
//Mart team adds Items / Products to the Inventory
//Mart team arranges some Items in Shopping Racks
//Consumer enters Mart and pulls a Cart
//Consumer shops Items from Shopping Racks to the Cart
//Consumer visits a Checkout counter
//Consumer hands over Items from the Cart to Checkout team
//Checkout team collects few details from Consumer: Payment Method, Billing Address, Shipping Address, Contact No/Email/Name
//Checkout team manages payment
//Checkout team hands over Payment Receipt and Items to Consumer
//Consumer exits Mart with Payment Receipt and Items
//~End~

@SpringBootTest //this annotation includes, @RunWith(SpringRunner.class)
public class InventoryManagementServiceIntegrationTest {

	Item BISCUIT_ITEM = new Item(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
	Item CHIVDA_ITEM = new Item(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
	Item BATHING_SOAP_ITEM = new Item(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
	Item PENCIL_ITEM = new Item(302, "Stationery", "Pencil", "Natraj Pencil", 10, 5.0);
	
	@Autowired
	private Inventory inventory;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@BeforeEach
	void setupBeforeEachTest() {

		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		BISCUIT_ITEM = inventory.addItem( BISCUIT_ITEM );
		CHIVDA_ITEM = inventory.addItem( CHIVDA_ITEM );
		BATHING_SOAP_ITEM = inventory.addItem( BATHING_SOAP_ITEM );
		
	}
	
	@AfterEach
	void cleanUpAfterEachTest() {
	
		itemRepository.deleteAll(); // necessary to ensure each test starts a clean
	}
	
	@Test
	public void addItemsAndGetItems() {
		
		itemRepository.deleteAll(); // just for this test, it is required to start a clean
		
		// initially its empty Inventory
		List<Item> items = inventory.getItems();
		assertNotNull(items);
		assertTrue(items.isEmpty());
		
		inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		inventory.addItem(BATHING_SOAP_ITEM);
		
		// check items after adding
		items = inventory.getItems();
		assertNotNull(items);
		assertFalse(items.isEmpty());
		System.out.println("\n\n~~~~~~0: Before: Size=" + items.size() + ", items="+ items + "\n");
		
		final int EXPECTED_ITEM_COUNT_1 = 
			BISCUIT_ITEM.getQuantity() + CHIVDA_ITEM.getQuantity() + BATHING_SOAP_ITEM.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT_1, inventory.getItemCount());
		
//		List<Item> itemList = itemRepository.findAll();
//		System.out.println("------------------------");
//		System.out.println(itemList);
//		System.out.println("------------------------");
//		int itemCount = 0;
//		for (Item item : itemList) {
//			itemCount += item.getQuantity();
//		}
//		assertEquals(EXPECTED_ITEM_COUNT_2, itemCount);
	}
	
	@Test
	void findSpecificItem() {
		Item persistedPencilItem = inventory.addItem(PENCIL_ITEM);
		Item item = inventory.getItem(persistedPencilItem.getItemId());
		assertNotNull(item);
	}
	
	@Test
	void searchItemsMatchingCategory() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Item exampleItem = new Item();
		exampleItem.setCategory(BISCUIT_ITEM.getCategory());
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(2, items.size());
		assertTrue(items.contains(BISCUIT_ITEM));
		assertTrue(items.contains(CHIVDA_ITEM));
		assertFalse(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	void searchItemsMatchingSubCategory() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Item exampleItem = new Item();
		exampleItem.setSubCategory(CHIVDA_ITEM.getSubCategory());
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(CHIVDA_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	void searchItemsMatchingName() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Item exampleItem = new Item();
		exampleItem.setName(BATHING_SOAP_ITEM.getName());
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
	}
	
	@Test
	void searchItemsMatchingQuantity() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
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
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Item exampleItem = new Item();
		exampleItem.setPrice(30.0);
		List<Item> items = inventory.searchItems(exampleItem);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
	}
	
	@Test
	void removeSpecificItem() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		inventory.removeItem(BISCUIT_ITEM);
		assertNull(inventory.getItem(BISCUIT_ITEM.getItemId()));
		assertNotNull(inventory.getItem(CHIVDA_ITEM.getItemId()));
	}

	@Test
	@Transactional // when modify (update/delete), that operation has to be Transactional
	void removeItemsMatchingGivenCriteria() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		// pattern-1
		Item exampleItem = new Item();
		exampleItem.setCategory("Grocery");
		System.out.println("\n\n~~~~~~1: Before: Size=" + inventory.getItems().size() + ", items="+ inventory.getItems() + "\n");
		inventory.removeItems(exampleItem);
		System.out.println("\n\n~~~~~~1: After: Size=" + inventory.getItems().size() + ", items="+ inventory.getItems() + "\n");
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
