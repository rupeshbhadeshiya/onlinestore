package com.learning.ddd.onlinestore.inventory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.learning.ddd.onlinestore.commons.util.HttpUtil;
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

@TestMethodOrder(OrderAnnotation.class)
public class InventoryAPITest {

	private static final String INVENTORY_SERVICE_URL = 
		"http://localhost:9010/inventory/items";

	private static final String INVENTORY_SEARCH_URL = INVENTORY_SERVICE_URL + "/searches";
	
	private InventoryItem BISCUIT_ITEM = new InventoryItem(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
	private InventoryItem CHIVDA_ITEM = new InventoryItem(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
	private InventoryItem BATHING_SOAP_ITEM = new InventoryItem(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
	private InventoryItem PENCIL_ITEM = new InventoryItem(302, "Stationery", "Pencil", "Natraj Pencil", 10, 5.0);

	private static int PENCIL_ITEM_ID;
	
//	@Autowired
//	private ItemRepository itemRepository;
	
	
//	@BeforeEach
//	void setupBeforeEachTest() {
//
//		// inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
//		
//		BISCUIT_ITEM = inventory.addItem( BISCUIT_ITEM );
//		CHIVDA_ITEM = inventory.addItem( CHIVDA_ITEM );
//		BATHING_SOAP_ITEM = inventory.addItem( BATHING_SOAP_ITEM );
//		
//	}
	
//	@AfterEach
//	void cleanUpAfterEachTest() {
//	
//		itemRepository.deleteAll(); // necessary to ensure each test starts a clean
//	}
	
	@Test
	@Order(1)
	void addItems() throws IOException {
		
		AddItemsRequestDTO requestDTO = new AddItemsRequestDTO(
			Arrays.asList( new InventoryItem[] { BISCUIT_ITEM, CHIVDA_ITEM, BATHING_SOAP_ITEM } )
		);
		
		AddItemsResponseDTO responseDTO = (AddItemsResponseDTO) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, AddItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> addedItems = responseDTO.getItems();
		assertNotNull(addedItems);
		assertEquals(3, addedItems.size());
		assertEquals(
				BISCUIT_ITEM.getQuantity()
				+ CHIVDA_ITEM.getQuantity() 
				+ BATHING_SOAP_ITEM.getQuantity(),
			responseDTO.getItemsQuantitiesTotal()
		);
		assertTrue(addedItems.contains(BISCUIT_ITEM));
		assertTrue(addedItems.contains(CHIVDA_ITEM));
		assertTrue(addedItems.contains(BATHING_SOAP_ITEM));
		
		for (InventoryItem item : addedItems) {
			assertTrue(item.getItemId() > 0);	// when entities are persisted they will be assigned unique id 
		}
	}
	
	@Test
	@Order(2)
	void getAllItems() throws IOException {
		
		AddItemsResponseDTO responseDTO = (AddItemsResponseDTO) HttpUtil.get(
			INVENTORY_SERVICE_URL, AddItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> addedItems = responseDTO.getItems();
		assertNotNull(addedItems);
		assertEquals(3, addedItems.size());
		assertEquals(
				BISCUIT_ITEM.getQuantity()
				+ CHIVDA_ITEM.getQuantity() 
				+ BATHING_SOAP_ITEM.getQuantity(),
			responseDTO.getItemsQuantitiesTotal()
		);
		assertTrue(addedItems.contains(BISCUIT_ITEM));
		assertTrue(addedItems.contains(CHIVDA_ITEM));
		assertTrue(addedItems.contains(BATHING_SOAP_ITEM));
		
		for (InventoryItem item : addedItems) {
			assertTrue(item.getItemId() > 0);	// when entities are persisted they will be assigned unique id 
		}
	}
	
	@Test
	@Order(3)
	void getSpecificItem() throws IOException {
		
		AddItemsRequestDTO requestDTO = new AddItemsRequestDTO(
			Arrays.asList( new InventoryItem[] { PENCIL_ITEM } )
		);
		
		AddItemsResponseDTO responseDTO = (AddItemsResponseDTO) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, AddItemsResponseDTO.class
		);
		assertNotNull(responseDTO);
		PENCIL_ITEM_ID = responseDTO.getItems().get(0).getItemId();
		
		InventoryItem pencilItem = (InventoryItem) HttpUtil.get(
			INVENTORY_SERVICE_URL + "/" + PENCIL_ITEM_ID, InventoryItem.class
		);
		
		assertNotNull(pencilItem);
		assertEquals(PENCIL_ITEM, pencilItem);
	}
	
	@Test
	@Order(4)
	void searchItemsMatchingCategory() throws IOException {
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setCategory(BISCUIT_ITEM.getCategory());
		
		SearchItemsRequestDTO requestDTO = new SearchItemsRequestDTO(exampleItem);
		
		SearchItemsResponseDTO responseDTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SERVICE_URL + "/searches", requestDTO, SearchItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> searchedItems = responseDTO.getItems();
		assertNotNull(searchedItems);
		assertEquals(2, searchedItems.size());
		assertTrue(searchedItems.contains(BISCUIT_ITEM));		// same category
		assertTrue(searchedItems.contains(CHIVDA_ITEM));		// same category
		assertFalse(searchedItems.contains(BATHING_SOAP_ITEM));	// different category
		assertFalse(searchedItems.contains(PENCIL_ITEM));		// different category
	}
	
	@Test
	@Order(5)
	void searchItemsMatchingSubCategory() throws IOException {
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setSubCategory(CHIVDA_ITEM.getSubCategory());
		
		SearchItemsRequestDTO requestDTO = new SearchItemsRequestDTO(exampleItem);
		
		SearchItemsResponseDTO responseDTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SERVICE_URL + "/searches", requestDTO, SearchItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> searchedItems = responseDTO.getItems();
		assertNotNull(searchedItems);
		assertEquals(1, searchedItems.size());
		assertTrue(searchedItems.contains(CHIVDA_ITEM));		// same sub-category
		assertFalse(searchedItems.contains(BISCUIT_ITEM));		// different sub-category
		assertFalse(searchedItems.contains(BATHING_SOAP_ITEM));	// different sub-category
		assertFalse(searchedItems.contains(PENCIL_ITEM));		// different sub-category
	}		
	
	@Test
	@Order(6)
	void searchItemsMatchingName() throws IOException {
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setName(BATHING_SOAP_ITEM.getName());
		
		SearchItemsRequestDTO requestDTO = new SearchItemsRequestDTO(exampleItem);
		
		SearchItemsResponseDTO responseDTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SERVICE_URL + "/searches", requestDTO, SearchItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> searchedItems = responseDTO.getItems();
		assertNotNull(searchedItems);
		assertEquals(1, searchedItems.size());
		assertTrue(searchedItems.contains(BATHING_SOAP_ITEM));	// correct name
		assertFalse(searchedItems.contains(BISCUIT_ITEM));		// different name
		assertFalse(searchedItems.contains(CHIVDA_ITEM));		// different name
		assertFalse(searchedItems.contains(PENCIL_ITEM));		// different name
	}
	
	@Test
	@Order(7)
	void searchItemsMatchingQuantity() throws IOException {
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setQuantity(10);
		
		SearchItemsRequestDTO requestDTO = new SearchItemsRequestDTO(exampleItem);
		
		SearchItemsResponseDTO responseDTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SERVICE_URL + "/searches", requestDTO, SearchItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> searchedItems = responseDTO.getItems();
		assertNotNull(searchedItems);
		assertEquals(3, searchedItems.size());
		assertTrue(searchedItems.contains(BISCUIT_ITEM));		// same size of items
		assertTrue(searchedItems.contains(CHIVDA_ITEM));		// same size of items
		assertTrue(searchedItems.contains(PENCIL_ITEM));		// same size of items
		assertFalse(searchedItems.contains(BATHING_SOAP_ITEM));	// different size of items
	}
	
	@Test
	@Order(8)
	void searchItemsMatchingPrice() throws IOException {
		
		InventoryItem exampleItem = new InventoryItem();
		exampleItem.setPrice(30.0);
		
		SearchItemsRequestDTO requestDTO = new SearchItemsRequestDTO(exampleItem);
		
		SearchItemsResponseDTO responseDTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SERVICE_URL + "/searches", requestDTO, SearchItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> searchedItems = responseDTO.getItems();
		assertNotNull(searchedItems);
		assertEquals(1, searchedItems.size());
		assertTrue(searchedItems.contains(BATHING_SOAP_ITEM));	// same item price
		assertFalse(searchedItems.contains(BISCUIT_ITEM));		// different item price
		assertFalse(searchedItems.contains(CHIVDA_ITEM));		// different item price
		assertFalse(searchedItems.contains(PENCIL_ITEM));		// different item price
	}
	
	@Test
	@Order(9)
	void removeSpecificItem() throws IOException {
		
		HttpUtil.delete(INVENTORY_SERVICE_URL + "/" + PENCIL_ITEM_ID);
		
		InventoryItem pencilItem = (InventoryItem) HttpUtil.get(
			INVENTORY_SERVICE_URL + "/" + PENCIL_ITEM_ID, AddItemsResponseDTO.class
		);
		
		assertNull(pencilItem);
	}

	@Test
	@Transactional // A modify operation (update/delete) has to be Transactional
	@Order(10)
	void removeItemsMatchingGivenCriteria() throws IOException {
		
		// pattern-1
		
		InventoryItem deleteExampleItem1 = new InventoryItem();
		deleteExampleItem1.setCategory("Grocery");
		DeleteItemsRequestDTO request1 = new DeleteItemsRequestDTO(deleteExampleItem1);
		
		HttpUtil.delete(INVENTORY_SERVICE_URL, request1);
		
		InventoryItem searchExampleItem1 = new InventoryItem();
		searchExampleItem1.setCategory("Grocery");
		SearchItemsRequestDTO request1DTO = new SearchItemsRequestDTO(searchExampleItem1);
		SearchItemsResponseDTO response1DTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SEARCH_URL, request1DTO, SearchItemsResponseDTO.class
		);
		assertNotNull(response1DTO);
		assertEquals(0, response1DTO.getItems().size());
		
		// pattern-2
		
		InventoryItem deleteExampleItem2 = deleteExampleItem1;
		deleteExampleItem2 = new InventoryItem();
		deleteExampleItem2.setCategory("Toiletries");
		DeleteItemsRequestDTO request2 = new DeleteItemsRequestDTO(deleteExampleItem2);
		
		HttpUtil.delete(INVENTORY_SERVICE_URL, request2);
		
		InventoryItem searchExampleItem2 = new InventoryItem();
		searchExampleItem2.setCategory("Toiletries");
		SearchItemsRequestDTO request2DTO = new SearchItemsRequestDTO(searchExampleItem2);
		SearchItemsResponseDTO response2DTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SEARCH_URL, request2DTO, SearchItemsResponseDTO.class
		);
		assertNotNull(response2DTO);
		assertEquals(0, response2DTO.getItems().size());
		
		// pattern-3
		
		InventoryItem deleteExampleItem3 = new InventoryItem();
		deleteExampleItem3.setName("Natraj Pencil");
		DeleteItemsRequestDTO request3 = new DeleteItemsRequestDTO(deleteExampleItem3);
		
		HttpUtil.delete(INVENTORY_SERVICE_URL, request3);
		
		InventoryItem searchExampleItem3 = new InventoryItem();
		searchExampleItem3.setName("Natraj Pencil");
		SearchItemsRequestDTO request3DTO = new SearchItemsRequestDTO(searchExampleItem3);
		SearchItemsResponseDTO response3DTO = (SearchItemsResponseDTO) HttpUtil.search(
			INVENTORY_SEARCH_URL, request3DTO, SearchItemsResponseDTO.class
		);
		assertNotNull(response3DTO);
		assertEquals(0, response3DTO.getItems().size());
		
//		InventoryItem deleteExampleItem3 = new InventoryItem();
//		deleteExampleItem3.setName("Parle-G");
//		DeleteItemsRequestDTO request3 = new DeleteItemsRequestDTO(deleteExampleItem3);
//		
//		HttpUtil.delete(INVENTORY_SERVICE_URL, request3);
//		
//		InventoryItem searchExampleItem3 = new InventoryItem();
//		searchExampleItem3.setName("Parle-G");
//		SearchItemsRequestDTO request3DTO = new SearchItemsRequestDTO(searchExampleItem3);
//		SearchItemsResponseDTO response3DTO = (SearchItemsResponseDTO) HttpUtil.search(
//			INVENTORY_SERVICE_URL, request3DTO, SearchItemsResponseDTO.class
//		);
//		assertNotNull(response3DTO);
//		assertEquals(0, response3DTO.getItems().size());
	}
	
}
