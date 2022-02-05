package com.learning.ddd.onlinestore.inventory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;

import com.learning.ddd.onlinestore.commons.util.HttpUtil;
import com.learning.ddd.onlinestore.inventory.application.ErrorDetails;
import com.learning.ddd.onlinestore.inventory.application.dto.AddItemRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.AddItemResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.DeleteItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.GetItemsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsResponseDTO;
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


/*
 * Test naming convention:
 * 		* Test is of application/domain behavior and not of code <- v. imp
 * 		* Test name should reflect domain behavior as <action><outcome>
 * 		* Test name does not have: input, output, process, function name
 * 		* Test name should separate words by underscore, for readability
 * 
 * 
 * Scenarios being tested:
 * 
 * You can add an InventoryItem with valid values
 * 		or, Adding an InventoryItem with valid values is allowed/accepted (/)
 * 		or, Adding an InventoryItem with valid values is success
 * 		or, Success in adding an InventoryItem with valid values
 * You cannot add an InventoryItem with invalid values
 * 		or, Adding an InventoryItem with invalid values is not allowed/rejected (/)
 * 		or, Adding an InventoryItem with invalid values is failure
 * 		or, Failure in adding an InventoryItem with invalid values
 * 
 * Adding an InventoryItem with valid values is successful
 * 
 * When try to add an InventoryItem with valid values of all fields then it is succeeded/accepted by system
 * When try to add an InventoryItem with invalid values of all fields then it is failed/rejected by system
 * 
 * 
 */

@TestMethodOrder(OrderAnnotation.class)
public class InventoryAPITest {

	private static final String ERROR_CATEGORY_IS_REQUIRED = "Category is required";
	private static final String ERROR_SUBCATEGORY_IS_REQUIRED = "Sub-Category is required";
	private static final String ERROR_NAME_IS_REQUIRED = "Name is required";
	private static final String ERROR_PRICE_IS_REQUIRED = "Price should be a positive decimal number";
	private static final String ERROR_QUANTITY_IS_REQUIRED = "Quantity should be a positive number";
	private static final String ERROR_ITEM_ALREADY_EXIST = "Item already exists";
	
	private static final String INVENTORY_SERVICE_URL = "http://localhost:9010/inventory/items";
	private static final String INVENTORY_SEARCH_URL = INVENTORY_SERVICE_URL + "/searches";
	
	private InventoryItem BISCUIT_ITEM = new InventoryItem("Grocery", "Biscuit", "Parle-G", 10.0, 10);
	private InventoryItem CHIVDA_ITEM = new InventoryItem("Grocery", "Chivda", "Real Farali Chivda", 20.0, 10);
	private InventoryItem BATHING_SOAP_ITEM = new InventoryItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, 5);
	private InventoryItem PENCIL_ITEM = new InventoryItem("Stationery", "Pencil", "Natraj Pencil", 5.0, 10);

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
	@org.junit.jupiter.api.Order(1)
	void adding_InventoryItem_with_valid_values() throws IOException {
	//void addingInventoryItemWithValidValues() throws IOException {
	//void addItem_Http201Created_Success() throws IOException {
		
		// Item1: Biscuit
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(BISCUIT_ITEM);
		AddItemResponseDTO responseDTO = (AddItemResponseDTO) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, AddItemResponseDTO.class
		);
		assertNotNull(responseDTO);
		InventoryItem addedItem = (InventoryItem) responseDTO.getItem();
		assertNotNull(addedItem);
		//assertEquals(BISCUIT_ITEM.getQuantity(), addedItem.getQuantity());
		assertTrue(BISCUIT_ITEM.equals(addedItem));
		assertTrue(addedItem.getItemId() > 0);	// when entities are persisted they will be assigned unique id 
		
		// Item2: Chivda
		requestDTO = new AddItemRequestDTO(CHIVDA_ITEM);
		responseDTO = (AddItemResponseDTO) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, AddItemResponseDTO.class
		);
		assertNotNull(responseDTO);
		addedItem = (InventoryItem) responseDTO.getItem();
		assertNotNull(addedItem);
		//assertEquals(CHIVDA_ITEM.getQuantity(), addedItem.getQuantity());
		assertTrue(CHIVDA_ITEM.equals(addedItem));
		assertTrue(addedItem.getItemId() > 0);	// when entities are persisted they will be assigned unique id 
		
		// Item3: Bathing Soap
		requestDTO = new AddItemRequestDTO(BATHING_SOAP_ITEM);
		responseDTO = (AddItemResponseDTO) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, AddItemResponseDTO.class
		);
		assertNotNull(responseDTO);
		addedItem = (InventoryItem) responseDTO.getItem();
		assertNotNull(addedItem);
		//assertEquals(BATHING_SOAP_ITEM.getQuantity(), addedItem.getQuantity());
		assertTrue(BATHING_SOAP_ITEM.equals(addedItem));
		assertTrue(addedItem.getItemId() > 0);	// when entities are persisted they will be assigned unique id
		
		// Item4: Pencil
		requestDTO = new AddItemRequestDTO(PENCIL_ITEM);
		responseDTO = (AddItemResponseDTO) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, AddItemResponseDTO.class
		);
		assertNotNull(responseDTO);
		addedItem = (InventoryItem) responseDTO.getItem();
		assertNotNull(addedItem);
		//assertEquals(BATHING_SOAP_ITEM.getQuantity(), addedItem.getQuantity());
		assertTrue(PENCIL_ITEM.equals(addedItem));
		assertTrue(addedItem.getItemId() > 0);	// when entities are persisted they will be assigned unique id
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	void adding_InventoryItem_with_invalid_values() throws IOException {
	//void adding_InventoryItem_with_invalid_values_is_not_allowed() throws IOException {
	//void addingInventoryItemWithInvalidValues() throws IOException {
	//void adding_inventoryItem_with_no_fields_is_invalid() throws IOException {
	//void adding_inventoryItem_with_no_fields_is_badRequest() throws IOException {
	//void givenAddItem_whenNoFields_thenHttp400BadRequest() throws IOException {
	//void addItem_Http400BadRequest_SpecifyNoField() throws IOException {
		
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(new InventoryItem());
		
		ErrorDetails errorDetails = (ErrorDetails) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, ErrorDetails.class
		);
		assertNotNull(errorDetails);
		assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetails.getStatus());
		assertNotNull(errorDetails.getErrors());
		assertTrue(errorDetails.getErrors().contains(ERROR_CATEGORY_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_SUBCATEGORY_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_NAME_IS_REQUIRED));
		//assertTrue(errorDetails.getErrors().contains(ERROR_PRICE_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_QUANTITY_IS_REQUIRED));
	}
	
	// scenario being tested: 
	//  When try to add an InventoryItem with invalid values for all fields 
	// 	then it is failed/rejected by system
	@Test
	@org.junit.jupiter.api.Order(3)
	void adding_InventoryItem_with_only_category() throws IOException {
	//void addingInventoryItemWithValidCategoryButOtherInvalidValues() throws IOException {
	//void adding_inventoryItem_with_only_category_is_invalid() throws IOException {
	//void addItem_Http400BadRequest_SpecifyOnlyCategory() throws IOException {
	//void givenAddItem_whenSpecifyOnlyCategory_thenHttp400BadRequest() throws IOException {
		
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(
			new InventoryItem(BISCUIT_ITEM.getCategory(), null, null, 0, 0)
		);
		ErrorDetails errorDetails = (ErrorDetails) HttpUtil.post(
				INVENTORY_SERVICE_URL, requestDTO, ErrorDetails.class
		);
		assertNotNull(errorDetails);
		assertFalse(errorDetails.getErrors().contains(ERROR_CATEGORY_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_SUBCATEGORY_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_NAME_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_PRICE_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_QUANTITY_IS_REQUIRED));
		
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	void adding_inventoryItem_with_only_category_and_subCategory() throws IOException {
	//void adding_inventoryItem_with_only_category_and_subCategory_is_invalid() throws IOException {
	//void addItem_Http400BadRequest_SpecifyOnlyCategoryAndSubCategory() throws IOException {
		
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(
			new InventoryItem(BISCUIT_ITEM.getCategory(), BISCUIT_ITEM.getSubCategory(), 
					null, 0, 0)
		);
		ErrorDetails errorDetails = (ErrorDetails) HttpUtil.post(
				INVENTORY_SERVICE_URL, requestDTO, ErrorDetails.class
		);
		assertNotNull(errorDetails);
		assertFalse(errorDetails.getErrors().contains(ERROR_CATEGORY_IS_REQUIRED));
		assertFalse(errorDetails.getErrors().contains(ERROR_SUBCATEGORY_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_NAME_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_PRICE_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_QUANTITY_IS_REQUIRED));
		
	}
	
	@Test
	@org.junit.jupiter.api.Order(5)
	void adding_InventoryItem_with_only_category_subCategory_and_name() throws IOException {
	//void addItem_Http400BadRequest_SpecifyOnlyCategorySubCategoryAndName() throws IOException {
		
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(
			new InventoryItem(BISCUIT_ITEM.getCategory(), BISCUIT_ITEM.getSubCategory(), 
					BISCUIT_ITEM.getName(), 0, 0)
		);
		ErrorDetails errorDetails = (ErrorDetails) HttpUtil.post(
				INVENTORY_SERVICE_URL, requestDTO, ErrorDetails.class
		);
		assertNotNull(errorDetails);
		assertFalse(errorDetails.getErrors().contains(ERROR_CATEGORY_IS_REQUIRED));
		assertFalse(errorDetails.getErrors().contains(ERROR_SUBCATEGORY_IS_REQUIRED));
		assertFalse(errorDetails.getErrors().contains(ERROR_NAME_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_PRICE_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_QUANTITY_IS_REQUIRED));

	}

	@Test
	@org.junit.jupiter.api.Order(6)
	void adding_InventoryItem_with_all_fields_except_quantity() throws IOException {
	//void addItem_Http400BadRequest_SpecifyAllFieldsExceptQuantity() throws IOException {
		
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(
			new InventoryItem(BISCUIT_ITEM.getCategory(), BISCUIT_ITEM.getSubCategory(), 
					BISCUIT_ITEM.getName(), BISCUIT_ITEM.getPrice(), 0)
		);
		ErrorDetails errorDetails = (ErrorDetails) HttpUtil.post(
				INVENTORY_SERVICE_URL, requestDTO, ErrorDetails.class
		);
		assertNotNull(errorDetails);
		assertFalse(errorDetails.getErrors().contains(ERROR_CATEGORY_IS_REQUIRED));
		assertFalse(errorDetails.getErrors().contains(ERROR_SUBCATEGORY_IS_REQUIRED));
		assertFalse(errorDetails.getErrors().contains(ERROR_NAME_IS_REQUIRED));
		assertFalse(errorDetails.getErrors().contains(ERROR_PRICE_IS_REQUIRED));
		assertTrue(errorDetails.getErrors().contains(ERROR_QUANTITY_IS_REQUIRED));
		
	}
	
	@Test
	@org.junit.jupiter.api.Order(7)
	void adding_InventoryItem_with_duplicate_values() throws IOException {
	//void adding_duplicate_InventoryItem_is_not_allowed() throws IOException {
	// void addItem_Http409Conflict_DuplicateRequest() throws IOException {
		
		// Scenario 1: Not specifying any field properly
		
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(BISCUIT_ITEM);
		
		ErrorDetails errorDetails = (ErrorDetails) HttpUtil.post(
			INVENTORY_SERVICE_URL, requestDTO, ErrorDetails.class
		);
		assertNotNull(errorDetails);
		assertEquals(HttpStatus.CONFLICT.value(), errorDetails.getStatus());
		assertNotNull(errorDetails.getErrors());
		assertTrue(errorDetails.getErrors().contains(ERROR_ITEM_ALREADY_EXIST));

	}
	
	@Test
	@org.junit.jupiter.api.Order(8)
	void retrieving_all_items() throws IOException {
		
		GetItemsResponseDTO responseDTO = (GetItemsResponseDTO) HttpUtil.get(
			INVENTORY_SERVICE_URL, GetItemsResponseDTO.class
		);
		
		assertNotNull(responseDTO);
		List<InventoryItem> addedItems = responseDTO.getItems();
		assertNotNull(addedItems);
		assertEquals(4, addedItems.size());
		assertEquals(
				BISCUIT_ITEM.getQuantity()
				+ CHIVDA_ITEM.getQuantity() 
				+ BATHING_SOAP_ITEM.getQuantity()
				+ PENCIL_ITEM.getQuantity(),
			responseDTO.getItemsQuantitiesTotal()
		);
		assertTrue(addedItems.contains(BISCUIT_ITEM));
		assertTrue(addedItems.contains(CHIVDA_ITEM));
		assertTrue(addedItems.contains(BATHING_SOAP_ITEM));
		assertTrue(addedItems.contains(PENCIL_ITEM));
		
		for (InventoryItem item : addedItems) {
			assertTrue(item.getItemId() > 0);	// when entities are persisted they will be assigned unique id 
		}
	}
	
	@Test
	@org.junit.jupiter.api.Order(9)
	void retrieving_a_specific_item() throws IOException {
		
		GetItemsResponseDTO getItemsResponseDTO = (GetItemsResponseDTO) HttpUtil.get(
			INVENTORY_SERVICE_URL, GetItemsResponseDTO.class
		);
		
		InventoryItem inventoryItem = getItemsResponseDTO.getItems().get(0);
		
		// way-1: finding specific item way-1: using get passing itemId
		
		InventoryItem item = (InventoryItem) HttpUtil.get(
			INVENTORY_SERVICE_URL + "/" + inventoryItem.getItemId(), InventoryItem.class
		);
		assertNotNull(item);
		assertEquals(inventoryItem, item);
		
//		// way-2: finding specific item way-2: using search passing unique attributes
//		
//		item = (InventoryItem) HttpUtil.get(
//			INVENTORY_SERVICE_URL + "?" + 
//				"category="+inventoryItem.getCategory()+
//				"&subCategory="+inventoryItem.getSubCategory()+
//				"&name="+inventoryItem.getName(), 
//			InventoryItem.class
//		);
//		assertNotNull(item);
//		assertEquals(inventoryItem, item);
	}
	
	@Test
	@org.junit.jupiter.api.Order(10)
	void searching_items_matching_category() throws IOException {
	//void searchItemsMatchingCategory_Http200Ok() throws IOException {
		
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
	@org.junit.jupiter.api.Order(11)
	void searching_items_matching_subCategory() throws IOException {
	//void searchItemsMatchingSubCategory_Http200Ok() throws IOException {
		
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
	@org.junit.jupiter.api.Order(12)
	void searching_items_matching_name() throws IOException {
	//void searchItemsMatchingName_Http200Ok() throws IOException {
		
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
	@org.junit.jupiter.api.Order(13)
	void searching_items_matching_quantity() throws IOException {
	//void searchItemsMatchingQuantity_Http200Ok() throws IOException {
		
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
	@org.junit.jupiter.api.Order(14)
	void searching_items_matching_price() throws IOException {
	//void searchItemsMatchingPrice_Http200Ok() throws IOException {
		
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
	@org.junit.jupiter.api.Order(15)
	void removing_a_specific_item() throws IOException {
	//void removeSpecificItem_Http204NoContent() throws IOException {

		GetItemsResponseDTO getItemsResponseDTO = (GetItemsResponseDTO) HttpUtil.get(
			INVENTORY_SERVICE_URL, GetItemsResponseDTO.class
		);
		
		int index = getItemsResponseDTO.getItems().indexOf(PENCIL_ITEM);
		InventoryItem pencilItem = getItemsResponseDTO.getItems().get(index);
		
		HttpUtil.delete(INVENTORY_SERVICE_URL + "/" + pencilItem.getItemId());
		
		assertNull( 
			HttpUtil.get(
				INVENTORY_SERVICE_URL + "/" + pencilItem.getItemId(), InventoryItem.class
			)
		);
	}

	@Test
	@Transactional // A modify operation (update/delete) has to be Transactional
	@org.junit.jupiter.api.Order(16)
	void removing_items_matching_given_criteria() throws IOException {
	//void removeItemsMatchingGivenCriteria_Http204NoContent() throws IOException {
		
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
