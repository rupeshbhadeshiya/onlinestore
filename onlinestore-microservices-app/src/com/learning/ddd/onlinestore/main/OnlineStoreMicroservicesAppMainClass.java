package com.learning.ddd.onlinestore.main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventService;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventSubscriber;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.DummyDomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.DummyDomainEventsSubscriber;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.Item;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.service.CheckoutService;
import com.learning.ddd.onlinestore.payment.domain.Address;
import com.learning.ddd.onlinestore.payment.domain.AddressType;
import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.payment.domain.DummyPaymentGateway;
import com.learning.ddd.onlinestore.shopping.domain.ShoppingCart;
import com.learning.ddd.onlinestore.transaction.domain.TransactionReceipt;

// Mart team adds Items in Inventory.... Inventory.addItems()... Item represents a product, item.getProduct()
// Consumer views Items... Inventory.getItems()
// Consumer shops items by adding them in ShoppingCart... ShoppingCart.addItems()
// Consumer visits Checkout counter where Mart team helps in payment processing...
//	 Order order = CheckoutService.checkout(shoppingCart), 
//	 order.setPaymentMethod()/ .setBillingAddress()/ .setShippingAddress()
// Consumer confirms payment of Order... TransactionReceipt txnReceipt = CheckoutService.processOrder()
// Mart Security team verifies PaymentReceipt and Items... Security.verify(txnReceipt, order) 
// Consumer looks up last X Orders... List<Order> lastXOrders = SearchOrdersService.search(criteria)
// Consumer views one of Order...
// Consumer may cancel latest/recent Order... order.cancel()
// Consumer may post Review and Ratings... item.getProduct().postReviewAndRatings()

public class OnlineStoreMicroservicesAppMainClass {

	public static void main(String[] args) {
		new OnlineStoreMicroservicesAppMainClass().startApp();
	}

	final Item BISCUIT_ITEM = new Item(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
	final Item CHIVDA_ITEM = new Item(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
	final Item BATHING_SOAP_ITEM = new Item(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);

	private DomainEventsPublisher domainEventPublisher;
	private DomainEventSubscriber domainEventSubscriber;
	private DomainEventService domainEventService;
	
	private Inventory inventory;
	private ShoppingCart cart;
	
	private CheckoutService checkoutService;
	private Order order;
	
	
	private void startApp() {
		
		setup();
		fillItemsInInventory();
		viewItemsFromInventory();
		shopItemsByAddingToCart();
		viewItemsInCart();
		checkout();
		
	}

	private void setup() {
		
		inventory = new Inventory();
		cart = new ShoppingCart();
		
		domainEventSubscriber = new DummyDomainEventsSubscriber();
		List<DomainEventSubscriber> domainEventSubscribers = new ArrayList<>();
		domainEventSubscribers.add(domainEventSubscriber);
		
		domainEventService = new DomainEventService();
		domainEventService.setDomainEventSubscribers(domainEventSubscribers);
		
		domainEventPublisher = new DummyDomainEventsPublisher();
		domainEventPublisher.setDomainEventService(domainEventService);
		
		inventory.setDomainEventPublisher(domainEventPublisher);
		cart.setDomainEventPublisher(domainEventPublisher);
		
		checkoutService = new CheckoutService();
		PaymentGateway samplePaymentGateway = new DummyPaymentGateway();
		checkoutService.setPaymentGateway(samplePaymentGateway);
	}
	
	private void fillItemsInInventory() {

		// add multiple items at a go
		inventory.addItems( Arrays.asList( new Item[] { BISCUIT_ITEM, CHIVDA_ITEM } ) );
		
		// add single item at a go
		inventory.addItem(BATHING_SOAP_ITEM);
		
		System.out.println("~~~~~~~~~~> inventory: " + inventory);
	}
	
	private void viewItemsFromInventory() {
		
		System.out.println( inventory.getItems() );
	}
	
	private void shopItemsByAddingToCart() {
		
		cart.addItem(BISCUIT_ITEM);
		cart.addItem(CHIVDA_ITEM);
		System.out.println("~~~~~~~~~~> cart: " + cart);
	}
	
	private void viewItemsInCart() {
		
		System.out.println( cart.getItems() );
	}
	
	
	private void checkout() {
		
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
		
		order = checkoutService.createOrder(cart, paymentMethod, billingAddress, shippingAddress);
		System.out.println("~~~~~~~~~~> order: " + order);
		
		TransactionReceipt transactionReceipt = checkoutService.checkout(order);
		System.out.println("~~~~~~~~~~> transactionReceipt: " + transactionReceipt);
	}
	
}
