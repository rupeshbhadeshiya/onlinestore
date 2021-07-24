package com.learning.ddd.onlinestore.inventory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

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
@TestMethodOrder(OrderAnnotation.class)
public class InventoryServiceTest {

	private InventoryItem BISCUIT_ITEM = new InventoryItem(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
	private InventoryItem CHIVDA_ITEM = new InventoryItem(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
	private InventoryItem BATHING_SOAP_ITEM = new InventoryItem(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
	private InventoryItem PENCIL_ITEM = new InventoryItem(302, "Stationery", "Pencil", "Natraj Pencil", 10, 5.0);
	
	@Autowired
	private Inventory inventory;
	
//	@Autowired
//	private static InventoryItemRepository itemRepository;
	
//	@BeforeEach
//	void setupBeforeEachTest() {
//		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
////		BISCUIT_ITEM = inventory.addItem( BISCUIT_ITEM );
////		CHIVDA_ITEM = inventory.addItem( CHIVDA_ITEM );
////		BATHING_SOAP_ITEM = inventory.addItem( BATHING_SOAP_ITEM );
//	}
	
//	@AfterEach
//	void cleanUpAfterEachTest() {
////		itemRepository.deleteAll(); // ensures that each test starts clean
//	}
	
//	@AfterAll
//	public static void cleanUpAfterAllTests() {
//		itemRepository.deleteAll(); // ensures that the next test cycle starts clean
//	}
	
	@Test
	@Order(1)
	void addItems() {
		
		List<InventoryItem> addedItems = inventory.addItems( Arrays.asList( 
				new InventoryItem[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		assertNotNull(addedItems);
		assertEquals(2, addedItems.size());
		int EXPECTED_ITEM_COUNT = BISCUIT_ITEM.getQuantity() + CHIVDA_ITEM.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT, inventory.getItemsQuantitiesTotal());
		
		InventoryItem addedItem = inventory.addItem(BATHING_SOAP_ITEM);
		assertNotNull(addedItem);
		EXPECTED_ITEM_COUNT = EXPECTED_ITEM_COUNT + BATHING_SOAP_ITEM.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT, inventory.getItemsQuantitiesTotal());
		
//		System.out.println(
//			"\n---------------------------------------"
//			+ "\n----[ Total Items Quantities = " + inventory.getItemsQuantitiesTotal() + " ]----"
//			+ "\n---------------------------------------"
//		);
		
	}
	
	@Test
	@Order(2)
	void getAllItems() {
		
		List<InventoryItem> items = inventory.getItems();
		assertNotNull(items);
		assertFalse(items.isEmpty());
		
		final int EXPECTED_ITEM_COUNT = BISCUIT_ITEM.getQuantity() 
										+ CHIVDA_ITEM.getQuantity() 
										+ BATHING_SOAP_ITEM.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT, inventory.getItemsQuantitiesTotal());
		
		assertTrue(items.contains(BISCUIT_ITEM));
		assertTrue(items.contains(CHIVDA_ITEM));
		assertTrue(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	@Order(3)
	void getSpecificItem() {
		
		InventoryItem persistedPencilItem = inventory.addItem(PENCIL_ITEM);
		
		InventoryItem item = inventory.getItem(persistedPencilItem.getItemId());
		
		assertNotNull(item);
	}
	
	@Test
	@Order(4)
	void searchItemsMatchingCategory() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setCategory(BISCUIT_ITEM.getCategory());
		
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		
		assertNotNull(items);
		assertEquals(2, items.size());
		assertTrue(items.contains(BISCUIT_ITEM));		// same category
		assertTrue(items.contains(CHIVDA_ITEM));		// same category
		assertFalse(items.contains(BATHING_SOAP_ITEM));	// different category
		assertFalse(items.contains(PENCIL_ITEM));		// different category
	}
	
	@Test
	@Order(5)
	void searchItemsMatchingSubCategory() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setSubCategory(CHIVDA_ITEM.getSubCategory());
		
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(CHIVDA_ITEM));		// same sub-category
		assertFalse(items.contains(BISCUIT_ITEM));		// different sub-category
		assertFalse(items.contains(BATHING_SOAP_ITEM));	// different sub-category
		assertFalse(items.contains(PENCIL_ITEM));		// different sub-category
	}
	
	@Test
	@Order(6)
	void searchItemsMatchingName() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setName(BATHING_SOAP_ITEM.getName());
		
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));	// correct name
		assertFalse(items.contains(BISCUIT_ITEM));		// different name
		assertFalse(items.contains(CHIVDA_ITEM));		// different name
		assertFalse(items.contains(PENCIL_ITEM));		// different name
	}
	
	@Test
	@Order(7)
	void searchItemsMatchingQuantity() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setQuantity(10);
		
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		
//		System.out.println(
//				"\n-----------searchItemsMatchingQuantity()--------------\n" +
//				items +
//				"\n------------------------------------------------------\n"
//			);
		
		assertNotNull(items);
		assertEquals(3, items.size());
		assertTrue(items.contains(BISCUIT_ITEM));
		assertTrue(items.contains(CHIVDA_ITEM));
		assertTrue(items.contains(PENCIL_ITEM));
		assertFalse(items.contains(BATHING_SOAP_ITEM));
	}
	
	@Test
	@Order(8)
	void searchItemsMatchingPrice() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setPrice(30.0);
		
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_ITEM));
		assertFalse(items.contains(BISCUIT_ITEM));
		assertFalse(items.contains(CHIVDA_ITEM));
		assertFalse(items.contains(PENCIL_ITEM));
	}
	
	@Test
	@Transactional // A modify operation (update/delete) has to be Transactional
	@Order(9)
	void removeSpecificItem() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		inventory.removeItem(PENCIL_ITEM);
		
		assertNull(inventory.getItem(PENCIL_ITEM.getItemId()));
	}

	@Test
	@Transactional // A modify operation (update/delete) has to be Transactional
	@Order(10)
	void removeItemsMatchingGivenCriteria() {
		
		//inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
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
