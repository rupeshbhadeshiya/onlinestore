package com.learning.ddd.onlinestore.inventory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.inventory.domain.exception.ProductAlreadyExistsException;
import com.learning.ddd.onlinestore.inventory.domain.repository.InventoryRepository;

// An Inventory contains Products; it may be referred as Product Store. So you don't need to create an Inventory.

//~Inventory-Specific~
// Add items to Inventory
// Get all items from Inventory
// Retrieve a specific item  from Inventory by its unique identifier
// Search items in Inventory through any combination of Product attributes
// Remove a specific item from Inventory
// Remove items from Inventory through any combination of attributes
//~End~

//~Overall~
//Mart has an Inventory (Storage space)
//Mart team adds Products / Products to the Inventory
//Mart team arranges some Products in Shopping Racks
//Consumer enters Mart and pulls a Cart
//Consumer shops Products from Shopping Racks to the Cart
//Consumer visits a Checkout counter
//Consumer hands over Products from the Cart to Checkout team
//Checkout team collects few details from Consumer: Payment Method, Billing Address, Shipping Address, Contact No/Email/Name
//Checkout team manages payment
//Checkout team hands over Payment Receipt and Products to Consumer
//Consumer exits Mart with Payment Receipt and Products
//~End~

@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest //this annotation includes, @RunWith(SpringRunner.class)
public class InventoryServiceTest {

	private static final int BISCUIT_INVENTORY_PRODUCT_ID = 11;
	private static final int BATHING_SOAP_INVENTORY_PRODUCT_ID = 22;
	private static final int CHIVDA_INVENTORY_PRODUCT_ID = 33;
	private static final int PENCIL_INVENTORY_PRODUCT_ID = 44;
	
	private static final int BISCUIT_INVENTORY_PRODUCT_QUANTITY = 10;
	private static final int BATHING_SOAP_INVENTORY_PRODUCT_QUANTITY = 5;
	private static final int CHIVDA_INVENTORY_PRODUCT_QUANTITY = 10;
	private static final int PENCIL_INVENTORY_PRODUCT_QUANTITY = 10;
	
	private final Product BISCUIT_PRODUCT = new Product(BISCUIT_INVENTORY_PRODUCT_ID, "Grocery", "Biscuit", "Parle-G", 10.0, BISCUIT_INVENTORY_PRODUCT_QUANTITY);
	private final Product BATHING_SOAP_PRODUCT = new Product(BATHING_SOAP_INVENTORY_PRODUCT_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, BATHING_SOAP_INVENTORY_PRODUCT_QUANTITY);
	private final Product CHIVDA_PRODUCT = new Product(CHIVDA_INVENTORY_PRODUCT_ID, "Grocery", "Chivda", "Real Farali Chivda", 20.0, CHIVDA_INVENTORY_PRODUCT_QUANTITY);
	private final Product PENCIL_PRODUCT = new Product(PENCIL_INVENTORY_PRODUCT_ID, "Stationery", "Pencil", "Natraj Pencil", 5.0, PENCIL_INVENTORY_PRODUCT_QUANTITY);
	
//	private final InventoryItem BISCUIT_PRODUCT = new InventoryItem(BISCUIT_PRODUCT);
//	private final InventoryItem BATHING_SOAP_PRODUCT = new InventoryItem(BATHING_SOAP_PRODUCT);
//	private final InventoryItem CHIVDA_PRODUCT = new InventoryItem(CHIVDA_PRODUCT);
//	private final InventoryItem PENCIL_PRODUCT = new InventoryItem(PENCIL_PRODUCT);

	@Autowired
	private Inventory inventory;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
//	@BeforeEach
//	void setupBeforeEachTest() {
//		//inventory.addProducts( Arrays.asList( new Product[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
////		BISCUIT_ITEM = inventory.addProduct( BISCUIT_ITEM );
////		CHIVDA_ITEM = inventory.addProduct( CHIVDA_ITEM );
////		BATHING_SOAP_ITEM = inventory.addProduct( BATHING_SOAP_ITEM );
//	}
	
	@AfterEach
	void cleanUpAfterEachTest() {
		// ensures to clean database after each test 
		// so that new test starts with clean database
		inventoryRepository.deleteAll(); 
	}
	
//	@AfterAll
//	public static void cleanUpAfterAllTests() {
//		itemRepository.deleteAll(); // ensures that the next test cycle starts clean
//	}
	
	@Test
	@org.junit.jupiter.api.Order(1)
	void addProductsAndGetAllAvailableProducts() throws ProductAlreadyExistsException, JMSException {
		
		List<Product> availableProductsInInventory;
		int EXPECTED_PRODUCT_COUNT, EXPECTED_PRODUCT_QUANTITIES_COUNT, DESIRED_PRODUCT_INDEX;

		// initially no products in inventory
		availableProductsInInventory = inventory.getAvailableProducts();
		assertTrue(availableProductsInInventory.isEmpty());
		
		// ... add 1st product ...
		Product addedProduct = inventory.addProduct(BISCUIT_PRODUCT);
		assertNotNull(addedProduct);
		
		EXPECTED_PRODUCT_COUNT = 1;
		EXPECTED_PRODUCT_QUANTITIES_COUNT = BISCUIT_PRODUCT.getQuantity();
		DESIRED_PRODUCT_INDEX = 0;
		
		// now inventory should have one product
		availableProductsInInventory = inventory.getAvailableProducts();
		assertNotNull(availableProductsInInventory);
		assertEquals(EXPECTED_PRODUCT_COUNT, availableProductsInInventory.size());
		assertEquals(EXPECTED_PRODUCT_QUANTITIES_COUNT, inventory.getProductQuantitiesTotal());
		assertEquals(BISCUIT_PRODUCT, availableProductsInInventory.get(DESIRED_PRODUCT_INDEX));
		
		//System.out.println(
		//	"\n---------------------------------------"
		//	+ "\n----[ #1 - Products in Inventory = " + inventory.getAvailableProducts() + " ]----"
		//	+ "\n---------------------------------------"
		//);
		
		// ... add 2nd product ...		
		addedProduct = inventory.addProduct(CHIVDA_PRODUCT);
		assertNotNull(addedProduct);
		
		EXPECTED_PRODUCT_COUNT = 2;
		EXPECTED_PRODUCT_QUANTITIES_COUNT += CHIVDA_PRODUCT.getQuantity();
		DESIRED_PRODUCT_INDEX = 1;
		
		// now inventory should have two products
		availableProductsInInventory = inventory.getAvailableProducts();
		assertNotNull(availableProductsInInventory);
		assertEquals(EXPECTED_PRODUCT_COUNT, availableProductsInInventory.size());
		assertEquals(EXPECTED_PRODUCT_QUANTITIES_COUNT, inventory.getProductQuantitiesTotal());
		assertEquals(CHIVDA_PRODUCT, availableProductsInInventory.get(DESIRED_PRODUCT_INDEX));
		
		
		// ... add 3rd product ...		
		addedProduct = inventory.addProduct(BATHING_SOAP_PRODUCT);
		assertNotNull(addedProduct);
		
		EXPECTED_PRODUCT_COUNT = 3;
		EXPECTED_PRODUCT_QUANTITIES_COUNT += BATHING_SOAP_PRODUCT.getQuantity();
		DESIRED_PRODUCT_INDEX = 2;
		
		// now inventory should have two products
		availableProductsInInventory = inventory.getAvailableProducts();
		assertNotNull(availableProductsInInventory);
		assertEquals(EXPECTED_PRODUCT_COUNT, availableProductsInInventory.size());
		assertEquals(EXPECTED_PRODUCT_QUANTITIES_COUNT, inventory.getProductQuantitiesTotal());
		assertEquals(BATHING_SOAP_PRODUCT, availableProductsInInventory.get(DESIRED_PRODUCT_INDEX));
	}
	
	@Test
	@org.junit.jupiter.api.Order(2)
	void addDuplicateProductToCatchProductsAlreadyExistsException() throws ProductAlreadyExistsException, JMSException {

		// set up
		
		Product addedProduct = inventory.addProduct(BISCUIT_PRODUCT);
		assertNotNull(addedProduct);
		
		// test
		
		ProductAlreadyExistsException ex = assertThrows(
			ProductAlreadyExistsException.class, () -> {
				inventory.addProduct(BISCUIT_PRODUCT);
			}
		);
		
		// validate
		
		assertNotNull(ex);
		assertEquals(BISCUIT_PRODUCT, ex.getProduct());
	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	void retrievingProductByProductId() throws ProductAlreadyExistsException, JMSException {
		
		Product addedProduct = inventory.addProduct(PENCIL_PRODUCT);
		
		Product pencilProduct = inventory.getProduct(addedProduct.getProductId());
		
		assertNotNull(pencilProduct);
		assertEquals(PENCIL_PRODUCT, pencilProduct);
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	void searchProductsMatchingCategory() throws ProductAlreadyExistsException, JMSException {
		
		// set up
		
		inventory.addProduct(BISCUIT_PRODUCT);
		inventory.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setCategory(BISCUIT_PRODUCT.getCategory());	// search by Category

		// test
		
		List<Product> products = inventory.searchProductsByExample(exampleProduct);
		
		// validate
		
		assertNotNull(products);
		assertEquals(2, products.size());						// 2 matching products to be found

		assertTrue(products.contains(BISCUIT_PRODUCT));			// same category
		assertTrue(products.contains(CHIVDA_PRODUCT));			// same category
		
		assertFalse(products.contains(BATHING_SOAP_PRODUCT));	// different category
		assertFalse(products.contains(PENCIL_PRODUCT));			// different category
	}
	
	@Test
	@org.junit.jupiter.api.Order(5)
	void searchProductsMatchingSubCategory() throws ProductAlreadyExistsException, JMSException {
		
		// set up
		
		inventory.addProduct(BISCUIT_PRODUCT);
		inventory.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setSubCategory(CHIVDA_PRODUCT.getSubCategory());	// search by SubCategory
		
		// test
		
		List<Product> products = inventory.searchProductsByExample(exampleProduct);
		
		// validate
		
		assertNotNull(products);
		assertEquals(1, products.size());						// 1 matching product to be found
		
		assertTrue(products.contains(CHIVDA_PRODUCT));			// same sub-category

		assertFalse(products.contains(BISCUIT_PRODUCT));		// different sub-category
		assertFalse(products.contains(BATHING_SOAP_PRODUCT));	// different sub-category
		assertFalse(products.contains(PENCIL_PRODUCT));			// different sub-category
	}
	
	@Test
	@org.junit.jupiter.api.Order(6)
	void searchProductsMatchingName() throws ProductAlreadyExistsException, JMSException {
		
		// set up
		
		inventory.addProduct(BISCUIT_PRODUCT);
		inventory.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setName(CHIVDA_PRODUCT.getName());
		
		// test
		
		List<Product> products = inventory.searchProductsByExample(exampleProduct);
		
		// validate
		
		assertNotNull(products);
		assertEquals(1, products.size());						// 1 matching product to be found
		
		assertTrue(products.contains(CHIVDA_PRODUCT));			// correct matching name
		
		assertFalse(products.contains(BISCUIT_PRODUCT));		// different name
		assertFalse(products.contains(BATHING_SOAP_PRODUCT));	// different name
		assertFalse(products.contains(PENCIL_PRODUCT));			// different name
	}
	
	@Test
	@org.junit.jupiter.api.Order(7)
	void searchProductsMatchingQuantity() throws ProductAlreadyExistsException, JMSException {
		
		// set up

		inventory.addProduct(BISCUIT_PRODUCT);
		inventory.addProduct(CHIVDA_PRODUCT);
		inventory.addProduct(PENCIL_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setQuantity(10);
		
		// test
		
		List<Product> products = inventory.searchProductsByExample(exampleProduct);
		
		// validate
		
		assertNotNull(products);
		assertEquals(3, products.size());						// 3 matching products to be found
		
		assertTrue(products.contains(BISCUIT_PRODUCT));			// same quantity product
		assertTrue(products.contains(CHIVDA_PRODUCT));			// same quantity product
		assertTrue(products.contains(PENCIL_PRODUCT));			// same quantity product
		
		assertFalse(products.contains(BATHING_SOAP_PRODUCT));	// different quantity product
	}
	
	@Test
	@org.junit.jupiter.api.Order(8)
	void searchProductsMatchingPrice() throws ProductAlreadyExistsException, JMSException {
		
		// set up

		inventory.addProduct(BISCUIT_PRODUCT);
		inventory.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setPrice(BISCUIT_PRODUCT.getPrice());
		
		// test
		
		List<Product> products = inventory.searchProductsByExample(exampleProduct);
		
		// validate
		
		assertNotNull(products);
		assertEquals(1, products.size());						// 1 matching product to be found
		
		assertTrue(products.contains(BISCUIT_PRODUCT));			// same price product
		
		assertFalse(products.contains(CHIVDA_PRODUCT));			// different price product
		assertFalse(products.contains(BATHING_SOAP_PRODUCT));	// different price product
		assertFalse(products.contains(PENCIL_PRODUCT));			// different price product
	}
	
	@Test
	@Transactional // A modify operation (update/delete) has to be Transactional
	@org.junit.jupiter.api.Order(9)
	void removeSpecificProduct() throws ProductAlreadyExistsException, JMSException {
		
		// set up

		inventory.addProduct(BISCUIT_PRODUCT);
		inventory.addProduct(CHIVDA_PRODUCT);
		
		// test
		
		inventory.removeProduct(PENCIL_PRODUCT.getProductId());
		
		// validate
		
		assertNull(inventory.getProduct(PENCIL_PRODUCT.getProductId()));
	}

//	@Test
//	@Transactional // A modify operation (update/delete) has to be Transactional
//	@org.junit.jupiter.api.Order(10)
//	void removeProductsMatchingGivenCriteria() {
//		
//		// set up
//
//		inventory.addProduct(BISCUIT_PRODUCT);
//		inventory.addProduct(CHIVDA_PRODUCT);
//		
//
//		// test and validate
//		
//		// pattern-1
//		Product exampleProduct = new Product();
//		exampleProduct.setCategory("Grocery");
//		inventory.removeProducts(exampleProduct);
//		assertTrue(inventory.searchProductsByExample(exampleProduct).isEmpty());
//		
//		// pattern-2
//		exampleProduct = new Product();
//		exampleProduct.setCategory("Toiletries");
//		inventory.removeProducts(exampleProduct);
//		assertTrue(inventory.searchProductsByExample(exampleProduct).isEmpty());
//		
//		// pattern-3
//		exampleProduct = new Product();
//		exampleProduct.setName("Parle-G");
//		inventory.removeProducts(exampleProduct);
//		assertTrue(inventory.searchProductsByExample(exampleProduct).isEmpty());
//	}
	
}
