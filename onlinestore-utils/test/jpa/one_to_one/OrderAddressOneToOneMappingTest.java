package jpa.one_to_one;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderAddressOneToOneMappingTest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_ID = 1001;
	private static final int BATHING_SOAP_ITEM_ID = 2002;
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private CartItem BISCUIT_CART_ITEM = new CartItem("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private CartItem BATHING_SOAP_CART_ITEM = new CartItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	private OrderItem BISCUIT_ORDER_ITEM = new OrderItem("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private OrderItem BATHING_SOAP_ORDER_ITEM = new OrderItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	@Autowired
	private OrderItemRepository itemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderService orderService;
	
	//@Test
	void oneToOneMappingBetweenOrderAndAddress() {
		
		//BISCUIT_ITEM = itemRepository.save(BISCUIT_ITEM);
		
//		Item biscuitItem = new Item("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
		
		Order order = new Order();
		
		//order.setName("Shriji");
		order.setPaymentMethod(PaymentMethod.NET_BANKING);
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		BISCUIT_ORDER_ITEM.setOrder(order);
		items.add(BISCUIT_ORDER_ITEM);
		order.setItems(items);
		
		Address bilAdrs = new Address();
		bilAdrs.setCountry("India");
		bilAdrs.setAddressType(AddressType.BILLING_ADDRESS);
		bilAdrs.setOrder(order);
		order.setBillingAddress(bilAdrs);
		
		Address shipAdrs = new Address();
		shipAdrs.setCountry("Bharat");
		shipAdrs.setAddressType(AddressType.SHIPPING_ADDRESS);
		shipAdrs.setOrder(order);
		order.setShippingAddress(shipAdrs);

		orderRepository.save(order);
		
		List<Order> orders = orderRepository.findAll();
		assertNotNull(orders);
		assertEquals(1, orders.size());
		
		Order o = orders.get(0);
		System.out.println("~~~~~~~~> : " + o);
		
		Address adr = o.getBillingAddress();
		System.out.println("~~~~~~~~> : " + adr);
		assertNotNull(adr);
		assertEquals(bilAdrs.getCountry(), adr.getCountry());
		
		Address otherAdr = o.getShippingAddress();
		System.out.println("~~~~~~~~> : " + otherAdr);
		assertNotNull(otherAdr);
		assertEquals(shipAdrs.getCountry(), otherAdr.getCountry());
	}
	
	@Test
	void createOrder() {
		
		Cart cart = new Cart(CONSUMER_ID);
		cart.addItem(BISCUIT_CART_ITEM);
		cart.addItem(BISCUIT_CART_ITEM);
		
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
		
		Order order = orderService.createOrder(
				cart, paymentMethod, billingAddress, shippingAddress
			);
			
		assertNotNull(order);
		assertNotNull(order.getItems());
		assertEquals(
			BISCUIT_ITEM_QUANTITY + BISCUIT_ITEM_QUANTITY, 
			order.getItemCount()
		);
		assertTrue(billingAddress.equalsContents(order.getBillingAddress()));
		assertTrue(shippingAddress.equalsContents(order.getShippingAddress()));
	}

}
