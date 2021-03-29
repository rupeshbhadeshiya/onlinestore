package backup_com.learning.ddd.onlinestore.order.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import backup_com.learning.ddd.onlinestore.payment.domain.Address;
import backup_com.learning.ddd.onlinestore.payment.domain.AddressRepository;
import backup_com.learning.ddd.onlinestore.payment.domain.AddressType;
import backup_com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

//
//When Consumer visits Checkout counter, Cart is transformed to Order
//~"Order represents a Consumer's purchase of Items from Cart"
//Thus it needs to include all details that represent this statement
//~"Order=Consumer+Items+Payment+Billing+Shipping+Summary+MartContact"
//

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

@SpringBootTest
class OrderServiceIntegrationTest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_ID = 1001;
	private static final int BATHING_SOAP_ITEM_ID = 2002;
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final Item BISCUIT_ITEM = new Item(BISCUIT_ITEM_ID, "Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final Item BATHING_SOAP_ITEM = new Item(BATHING_SOAP_ITEM_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
//	@BeforeEach
//	void beforeEachTest() {
//		orderRepository.deleteAll(); // needed to ensure each Test starts a clean
//	}
	
	@Test
	void createAndRetrieveAddress() {
		
		Address a = new Address();
		a.setCountry("Bharat");
		a.setAddressType(AddressType.SHIPPING_ADDRESS);
		
		addressRepository.save(a);
		
		List<Address> addresses = addressRepository.findAll();
		assertNotNull(addresses);
		assertEquals(1, addresses.size());
		
		Address aFromDB = addresses.get(0);
		assertEquals(a.getCountry(), aFromDB.getCountry());
		assertEquals(a.getAddressType(), aFromDB.getAddressType());
	}
	
	@Test
	void createOrder() {

		Cart cart = new Cart(CONSUMER_ID);
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(BATHING_SOAP_ITEM);
		
		PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
		
		Address billingAddress = new Address(
				AddressType.BILLING_ADDRESS, 
				"l1", "l2", "l3", "l4", 
				"pincode", "state", "country"
			);
		Address shippingAddress = new Address(
				AddressType.SHIPPING_ADDRESS, 
				"l1", "l2", "l3", "l4", 
				"pincode", "state", "country"
			); 
		
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		
		orderRepository.save(order);
		
		List<Order> orders = orderRepository.findAll();
		assertNotNull(orders);
		assertEquals(1,  orders.size());
		Order ord = orders.get(0);
		assertEquals(AddressType.BILLING_ADDRESS, ord.getBillingAddress().getAddressType());
		assertEquals(AddressType.SHIPPING_ADDRESS, ord.getShippingAddress().getAddressType());
		
		
//		Order order = orderService.createOrder(cart, paymentMethod,
//			billingAddress, shippingAddress);
//		
//		assertNotNull(order);
//		assertNotNull(order.getItems());
//		assertEquals(
//			BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, 
//			order.getItemCount()
//		);
	}

}
