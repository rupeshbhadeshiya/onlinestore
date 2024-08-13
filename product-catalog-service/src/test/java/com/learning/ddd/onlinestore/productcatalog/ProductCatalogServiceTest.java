package com.learning.ddd.onlinestore.productcatalog;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.learning.ddd.onlinestore.productcatalog.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.domain.exception.ProductAlreadyExistsException;
import com.learning.ddd.onlinestore.productcatalog.domain.service.ProductCatalogService;

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


@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest //this annotation includes, @RunWith(SpringRunner.class)
public class ProductCatalogServiceTest {

	private Product BISCUIT_PRODUCT = new Product("Grocery", "Biscuit", "Parle-G", 10.0, 10);
	private Product CHIVDA_PRODUCT = new Product("Grocery", "Chivda", "Real Farali Chivda", 20.0, 10);
	private Product BATHING_SOAP_PRODUCT = new Product("Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, 5);
	private Product PENCIL_PRODUCT = new Product("Stationery", "Pencil", "Natraj Pencil", 5.0, 10);

	@Autowired
	private ProductCatalogService productcatalogService;
	
//	@Autowired
//	private static InventoryItemRepository itemRepository;
	
//	@BeforeEach
//	void setupBeforeEachTest() {
//		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
////		BISCUIT_ITEM = inventory.addProduct( BISCUIT_ITEM );
////		CHIVDA_ITEM = inventory.addProduct( CHIVDA_ITEM );
////		BATHING_SOAP_ITEM = inventory.addProduct( BATHING_SOAP_ITEM );
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
	@org.junit.jupiter.api.Order(1)
	void addProducts() throws ProductAlreadyExistsException, JMSException {
		
		Product addedItem = productcatalogService.addProduct(BISCUIT_PRODUCT);
		assertNotNull(addedItem);
		int EXPECTED_ITEM_COUNT = BISCUIT_PRODUCT.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT, productcatalogService.getItemsQuantitiesTotal());
		
		addedItem = productcatalogService.addProduct(CHIVDA_PRODUCT);
		assertNotNull(addedItem);
		EXPECTED_ITEM_COUNT += CHIVDA_PRODUCT.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT, productcatalogService.getItemsQuantitiesTotal());
		
		addedItem = productcatalogService.addProduct(BATHING_SOAP_PRODUCT);
		assertNotNull(addedItem);
		EXPECTED_ITEM_COUNT += BATHING_SOAP_PRODUCT.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT, productcatalogService.getItemsQuantitiesTotal());

//		System.out.println(
//			"\n---------------------------------------"
//			+ "\n----[ Total Items Quantities = " + inventory.getItemsQuantitiesTotal() + " ]----"
//			+ "\n---------------------------------------"
//		);
		
	}
	
	@Test
	@org.junit.jupiter.api.Order(2)
	void addProductsCatchItemsAlreadyExistsException() {

		// execute
		
		ProductAlreadyExistsException ex = assertThrows(
			ProductAlreadyExistsException.class, () -> {
				productcatalogService.addProduct(BISCUIT_PRODUCT);
			}
		);
		
		// validate
		
		assertNotNull(ex);
		assertEquals(BISCUIT_PRODUCT, ex.getProduct());
	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	void getAllItems() {
		
		List<Product> items = productcatalogService.getProducts();
		assertNotNull(items);
		assertFalse(items.isEmpty());
		
		final int EXPECTED_ITEM_COUNT = BISCUIT_PRODUCT.getQuantity() 
										+ CHIVDA_PRODUCT.getQuantity() 
										+ BATHING_SOAP_PRODUCT.getQuantity();
		assertEquals(EXPECTED_ITEM_COUNT, productcatalogService.getItemsQuantitiesTotal());
		
		assertTrue(items.contains(BISCUIT_PRODUCT));
		assertTrue(items.contains(CHIVDA_PRODUCT));
		assertTrue(items.contains(BATHING_SOAP_PRODUCT));
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	void getSpecificItem() throws ProductAlreadyExistsException, JMSException {
		
		Product persistedPencilItem = productcatalogService.addProduct(PENCIL_PRODUCT);
		
		Product item = productcatalogService.getProduct(persistedPencilItem.getProductId());
		
		assertNotNull(item);
	}
	
	@Test
	@org.junit.jupiter.api.Order(5)
	void searchItemsMatchingCategory() {
		
		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Product exampleItem = new Product();
		exampleItem.setCategory(BISCUIT_PRODUCT.getCategory());
		
		List<Product> items = productcatalogService.searchProducts(exampleItem);
		
		assertNotNull(items);
		assertEquals(2, items.size());
		assertTrue(items.contains(BISCUIT_PRODUCT));		// same category
		assertTrue(items.contains(CHIVDA_PRODUCT));		// same category
		assertFalse(items.contains(BATHING_SOAP_PRODUCT));	// different category
		assertFalse(items.contains(PENCIL_PRODUCT));		// different category
	}
	
	@Test
	@org.junit.jupiter.api.Order(6)
	void searchItemsMatchingSubCategory() {
		
		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Product exampleItem = new Product();
		exampleItem.setSubCategory(CHIVDA_PRODUCT.getSubCategory());
		
		List<Product> items = productcatalogService.searchProducts(exampleItem);
		
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(CHIVDA_PRODUCT));		// same sub-category
		assertFalse(items.contains(BISCUIT_PRODUCT));		// different sub-category
		assertFalse(items.contains(BATHING_SOAP_PRODUCT));	// different sub-category
		assertFalse(items.contains(PENCIL_PRODUCT));		// different sub-category
	}
	
	@Test
	@org.junit.jupiter.api.Order(7)
	void searchItemsMatchingName() {
		
		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Product exampleItem = new Product();
		exampleItem.setName(BATHING_SOAP_PRODUCT.getName());
		
		List<Product> items = productcatalogService.searchProducts(exampleItem);
		
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_PRODUCT));	// correct name
		assertFalse(items.contains(BISCUIT_PRODUCT));		// different name
		assertFalse(items.contains(CHIVDA_PRODUCT));		// different name
		assertFalse(items.contains(PENCIL_PRODUCT));		// different name
	}
	
	@Test
	@org.junit.jupiter.api.Order(8)
	void searchItemsMatchingQuantity() {
		
		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Product exampleItem = new Product();
		exampleItem.setQuantity(10);
		
		List<Product> items = productcatalogService.searchProducts(exampleItem);
		
//		System.out.println(
//				"\n-----------searchItemsMatchingQuantity()--------------\n" +
//				items +
//				"\n------------------------------------------------------\n"
//			);
		
		assertNotNull(items);
		assertEquals(3, items.size());
		assertTrue(items.contains(BISCUIT_PRODUCT));
		assertTrue(items.contains(CHIVDA_PRODUCT));
		assertTrue(items.contains(PENCIL_PRODUCT));
		assertFalse(items.contains(BATHING_SOAP_PRODUCT));
	}
	
	@Test
	@org.junit.jupiter.api.Order(9)
	void searchItemsMatchingPrice() {
		
		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		Product exampleItem = new Product();
		exampleItem.setPrice(30.0);
		
		List<Product> items = productcatalogService.searchProducts(exampleItem);
		
		assertNotNull(items);
		assertEquals(1, items.size());
		assertTrue(items.contains(BATHING_SOAP_PRODUCT));
		assertFalse(items.contains(BISCUIT_PRODUCT));
		assertFalse(items.contains(CHIVDA_PRODUCT));
		assertFalse(items.contains(PENCIL_PRODUCT));
	}
	
	@Test
	@Transactional // A modify operation (update/delete) has to be Transactional
	@org.junit.jupiter.api.Order(10)
	void removeSpecificItem() throws CloneNotSupportedException, JMSException {
		
		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		productcatalogService.removeProduct(PENCIL_PRODUCT);
		
		assertNull(productcatalogService.getProduct(PENCIL_PRODUCT.getProductId()));
	}

	@Test
	@Transactional // A modify operation (update/delete) has to be Transactional
	@org.junit.jupiter.api.Order(11)
	void removeItemsMatchingGivenCriteria() {
		
		//inventory.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		// pattern-1
		Product exampleItem = new Product();
		exampleItem.setCategory("Grocery");
		productcatalogService.removeProducts(exampleItem);
		assertTrue(productcatalogService.searchProducts(exampleItem).isEmpty());
		
		// pattern-2
		exampleItem = new Product();
		exampleItem.setCategory("Toiletries");
		productcatalogService.removeProducts(exampleItem);
		assertTrue(productcatalogService.searchProducts(exampleItem).isEmpty());
		
		// pattern-3
		exampleItem = new Product();
		exampleItem.setName("Parle-G");
		productcatalogService.removeProducts(exampleItem);
		assertTrue(productcatalogService.searchProducts(exampleItem).isEmpty());
	}
	
}
