package com.learning.ddd.onlinestore.productcatalog;

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

import com.learning.ddd.onlinestore.product.domain.Product;
import com.learning.ddd.onlinestore.product.domain.exception.ProductAlreadyExistsException;
import com.learning.ddd.onlinestore.productcatalog.domain.repository.ProductCatalogRepository;
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

	private static final int BISCUIT_PRODUCT_ID = 11;
	private static final int BATHING_SOAP_PRODUCT_ID = 22;
	private static final int CHIVDA_PRODUCT_ID = 33;
	private static final int PENCIL_PRODUCT_ID = 44;
	
	private static final int BISCUIT_PRODUCT_QUANTITY = 10;
	private static final int BATHING_SOAP_PRODUCT_QUANTITY = 5;
	private static final int CHIVDA_PRODUCT_QUANTITY = 10;
	private static final int PENCIL_PRODUCT_QUANTITY = 10;
	
	private final Product BISCUIT_PRODUCT = new Product(BISCUIT_PRODUCT_ID, "Grocery", "Biscuit", "Parle-G", 10.0, BISCUIT_PRODUCT_QUANTITY);
	private final Product BATHING_SOAP_PRODUCT = new Product(BATHING_SOAP_PRODUCT_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, BATHING_SOAP_PRODUCT_QUANTITY);
	private final Product CHIVDA_PRODUCT = new Product(CHIVDA_PRODUCT_ID, "Grocery", "Chivda", "Real Farali Chivda", 20.0, CHIVDA_PRODUCT_QUANTITY);
	private final Product PENCIL_PRODUCT = new Product(PENCIL_PRODUCT_ID, "Stationery", "Pencil", "Natraj Pencil", 5.0, PENCIL_PRODUCT_QUANTITY);
	
//	private final ProductCatalogItem BISCUIT_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(BISCUIT_PRODUCT);
//	private final ProductCatalogItem BATHING_SOAP_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(BATHING_SOAP_PRODUCT);
//	private final ProductCatalogItem CHIVDA_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(CHIVDA_PRODUCT);
//	private final ProductCatalogItem PENCIL_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(PENCIL_PRODUCT);
	
	@Autowired
	private ProductCatalogService productCatalogService;
	
	@Autowired
	private ProductCatalogRepository productCatalogRepository;
	
//	@BeforeEach
//	void setupBeforeEachTest() {
//		//productCatalogService.addProducts( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
////		BISCUIT_ITEM = productCatalogService.addProduct( BISCUIT_ITEM );
////		CHIVDA_ITEM = productCatalogService.addProduct( CHIVDA_ITEM );
////		BATHING_SOAP_ITEM = productCatalogService.addProduct( BATHING_SOAP_ITEM );
//	}
	
	@AfterEach
	void cleanUpAfterEachTest() {
		// ensures to clean database after each test 
		// so that new test starts with clean database
		productCatalogRepository.deleteAll(); 
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
		availableProductsInInventory = productCatalogService.getAllProducts();
		assertTrue(availableProductsInInventory.isEmpty());
		
		// ... add 1st product ...
		Product addedProduct = productCatalogService.addProduct(BISCUIT_PRODUCT);
		assertNotNull(addedProduct);
		
		EXPECTED_PRODUCT_COUNT = 1;
		EXPECTED_PRODUCT_QUANTITIES_COUNT = BISCUIT_PRODUCT.getQuantity();
		DESIRED_PRODUCT_INDEX = 0;
		
		// now inventory should have one product
		availableProductsInInventory = productCatalogService.getAllProducts();
		assertNotNull(availableProductsInInventory);
		assertEquals(EXPECTED_PRODUCT_COUNT, availableProductsInInventory.size());
		assertEquals(EXPECTED_PRODUCT_QUANTITIES_COUNT, productCatalogService.getProductQuantitiesTotal());
		assertEquals(BISCUIT_PRODUCT, availableProductsInInventory.get(DESIRED_PRODUCT_INDEX));
		
		//System.out.println(
		//	"\n---------------------------------------"
		//	+ "\n----[ #1 - Products in Inventory = " + productCatalogService.getAllProducts() + " ]----"
		//	+ "\n---------------------------------------"
		//);
		
		// ... add 2nd product ...		
		addedProduct = productCatalogService.addProduct(CHIVDA_PRODUCT);
		assertNotNull(addedProduct);
		
		EXPECTED_PRODUCT_COUNT = 2;
		EXPECTED_PRODUCT_QUANTITIES_COUNT += CHIVDA_PRODUCT.getQuantity();
		DESIRED_PRODUCT_INDEX = 1;
		
		// now inventory should have two products
		availableProductsInInventory = productCatalogService.getAllProducts();
		assertNotNull(availableProductsInInventory);
		assertEquals(EXPECTED_PRODUCT_COUNT, availableProductsInInventory.size());
		assertEquals(EXPECTED_PRODUCT_QUANTITIES_COUNT, productCatalogService.getProductQuantitiesTotal());
		assertEquals(CHIVDA_PRODUCT, availableProductsInInventory.get(DESIRED_PRODUCT_INDEX));
		
		
		// ... add 3rd product ...		
		addedProduct = productCatalogService.addProduct(BATHING_SOAP_PRODUCT);
		assertNotNull(addedProduct);
		
		EXPECTED_PRODUCT_COUNT = 3;
		EXPECTED_PRODUCT_QUANTITIES_COUNT += BATHING_SOAP_PRODUCT.getQuantity();
		DESIRED_PRODUCT_INDEX = 2;
		
		// now inventory should have two products
		availableProductsInInventory = productCatalogService.getAllProducts();
		assertNotNull(availableProductsInInventory);
		assertEquals(EXPECTED_PRODUCT_COUNT, availableProductsInInventory.size());
		assertEquals(EXPECTED_PRODUCT_QUANTITIES_COUNT, productCatalogService.getProductQuantitiesTotal());
		assertEquals(BATHING_SOAP_PRODUCT, availableProductsInInventory.get(DESIRED_PRODUCT_INDEX));
	}
	
	@Test
	@org.junit.jupiter.api.Order(2)
	void addDuplicateProductToCatchProductsAlreadyExistsException() throws ProductAlreadyExistsException, JMSException {

		// set up
		
		Product addedProduct = productCatalogService.addProduct(BISCUIT_PRODUCT);
		assertNotNull(addedProduct);
		
		// test
		
		ProductAlreadyExistsException ex = assertThrows(
			ProductAlreadyExistsException.class, () -> {
				productCatalogService.addProduct(BISCUIT_PRODUCT);
			}
		);
		
		// validate
		
		assertNotNull(ex);
		assertEquals(BISCUIT_PRODUCT, ex.getProduct());
	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	void retrievingProductByProductId() throws ProductAlreadyExistsException, JMSException {
		
		Product addedProduct = productCatalogService.addProduct(PENCIL_PRODUCT);
		
		Product pencilProduct = productCatalogService.getProduct(addedProduct.getProductId());
		
		assertNotNull(pencilProduct);
		assertEquals(PENCIL_PRODUCT, pencilProduct);
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	void searchProductsMatchingCategory() throws ProductAlreadyExistsException, JMSException {
		
		// set up
		
		productCatalogService.addProduct(BISCUIT_PRODUCT);
		productCatalogService.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setCategory(BISCUIT_PRODUCT.getCategory());	// search by Category

		// test
		
		List<Product> products = productCatalogService.searchProductsByExample(exampleProduct);
		
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
		
		productCatalogService.addProduct(BISCUIT_PRODUCT);
		productCatalogService.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setSubCategory(CHIVDA_PRODUCT.getSubCategory());	// search by SubCategory
		
		// test
		
		List<Product> products = productCatalogService.searchProductsByExample(exampleProduct);
		
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
		
		productCatalogService.addProduct(BISCUIT_PRODUCT);
		productCatalogService.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setName(CHIVDA_PRODUCT.getName());
		
		// test
		
		List<Product> products = productCatalogService.searchProductsByExample(exampleProduct);
		
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

		productCatalogService.addProduct(BISCUIT_PRODUCT);
		productCatalogService.addProduct(CHIVDA_PRODUCT);
		productCatalogService.addProduct(PENCIL_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setQuantity(10);
		
		// test
		
		List<Product> products = productCatalogService.searchProductsByExample(exampleProduct);
		
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

		productCatalogService.addProduct(BISCUIT_PRODUCT);
		productCatalogService.addProduct(CHIVDA_PRODUCT);
		
		Product exampleProduct = new Product();
		exampleProduct.setPrice(BISCUIT_PRODUCT.getPrice());
		
		// test
		
		List<Product> products = productCatalogService.searchProductsByExample(exampleProduct);
		
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

		productCatalogService.addProduct(BISCUIT_PRODUCT);
		productCatalogService.addProduct(CHIVDA_PRODUCT);
		
		// test
		
		productCatalogService.removeProduct(PENCIL_PRODUCT.getProductId());
		
		// validate
		
		assertNull(productCatalogService.getProduct(PENCIL_PRODUCT.getProductId()));
	}

//	@Test
//	@Transactional // A modify operation (update/delete) has to be Transactional
//	@org.junit.jupiter.api.Order(10)
//	void removeProductsMatchingGivenCriteria() {
//		
//		// set up
//
//		productCatalogService.addProduct(BISCUIT_PRODUCT);
//		productCatalogService.addProduct(CHIVDA_PRODUCT);
//		
//
//		// test and validate
//		
//		// pattern-1
//		Product exampleProduct = new Product();
//		exampleProduct.setCategory("Grocery");
//		productCatalogService.removeProducts(exampleProduct);
//		assertTrue(productCatalogService.searchProductsByExample(exampleProduct).isEmpty());
//		
//		// pattern-2
//		exampleProduct = new Product();
//		exampleProduct.setCategory("Toiletries");
//		productCatalogService.removeProducts(exampleProduct);
//		assertTrue(productCatalogService.searchProductsByExample(exampleProduct).isEmpty());
//		
//		// pattern-3
//		exampleProduct = new Product();
//		exampleProduct.setName("Parle-G");
//		productCatalogService.removeProducts(exampleProduct);
//		assertTrue(productCatalogService.searchProductsByExample(exampleProduct).isEmpty());
//	}
	
}
