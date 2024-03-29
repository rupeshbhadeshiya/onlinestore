package com.learning.ddd.onlinestore.checkout.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learning.ddd.onlinestore.checkout.domain.repository.OrderRepository;
import com.learning.ddd.onlinestore.checkout.domain.service.OrderService;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

//
//When Consumer visits Checkout counter, Cart is transformed to Order
//~"Order represents a Consumer's purchase of Items from Cart"
//Thus it needs to include all details that represent this statement
//~"Order=Consumer+Items+Payment+Billing+Shipping+Summary+Store-Contact"
//

//~Checkout-Specific~
//Convert a Cart to an Order
//Retrieve all Orders of a Consumer
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

@SpringBootTest
public class CheckoutServiceIntegrationTest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private CartItem BISCUIT_CART_ITEM = new CartItem("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private CartItem BATHING_SOAP_CART_ITEM = new CartItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderService orderService;
	
	@BeforeEach
	void setupForEachTest() {
		// empty for now!
	}
	
	@AfterEach
	void cleanupForEachTest() {
		orderRepository.deleteAll();
	}
	
	@Test
	void createOrder() {
		
		Cart cart = new Cart(CONSUMER_ID);
		
		BISCUIT_CART_ITEM.setCart(cart);	// establish bi-directional (this is one direction)
		cart.addItem(BISCUIT_CART_ITEM);	// establish bi-directional (this is other direction)
		
		BATHING_SOAP_CART_ITEM.setCart(cart);	// establish bi-directional (this is one direction)
		cart.addItem(BATHING_SOAP_CART_ITEM);	// establish bi-directional (this is other direction)
		
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
			BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, 
			order.getItemCount()
		);
		assertTrue(billingAddress.equalsContents(order.getBillingAddress()));
		assertTrue(shippingAddress.equalsContents(order.getShippingAddress()));
		
		// verify other way around!
		
		List<Order> orders = orderRepository.findAll();
		assertNotNull(orders);
		assertEquals(1, orders.size());
		
		Order o = orders.get(0);
		//System.out.println("~~~~~~~~> : " + o);
		
		Address adr = o.getBillingAddress();
		//System.out.println("~~~~~~~~> : " + adr);
		assertNotNull(adr);
		assertEquals(billingAddress.getCountry(), adr.getCountry());
		
		Address otherAdr = o.getShippingAddress();
		//System.out.println("~~~~~~~~> : " + otherAdr);
		assertNotNull(otherAdr);
		assertEquals(shippingAddress.getCountry(), otherAdr.getCountry());
	}
	
	@Test
	void getAllOrders() {
		
		// initially, no order!
		List<Order> orders = orderService.getOrders(CONSUMER_ID);
		assertEquals(0, orders.size());
		
		// add one order and verify
		
		Cart cart = new Cart(CONSUMER_ID);
		
		BISCUIT_CART_ITEM.setCart(cart);	// establish bi-directional (this is one direction)
		cart.addItem(BISCUIT_CART_ITEM);	// establish bi-directional (this is other direction)
		
		BATHING_SOAP_CART_ITEM.setCart(cart);	// establish bi-directional (this is one direction)
		cart.addItem(BATHING_SOAP_CART_ITEM);	// establish bi-directional (this is other direction)
		
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
		
		orderService.createOrder(
			cart, paymentMethod, billingAddress, shippingAddress
		);
			
		orders = orderService.getOrders(CONSUMER_ID);
		assertEquals(1, orders.size());
		
		// add another order and verify
	}
	
}
