package com.learning.ddd.onlinestore.order.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventService;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventSubscriber;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.DummyDomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.DummyDomainEventsSubscriber;
import com.learning.ddd.onlinestore.inventory.domain.Item;
import com.learning.ddd.onlinestore.order.domain.service.CheckoutService;
import com.learning.ddd.onlinestore.payment.domain.AddressType;
import com.learning.ddd.onlinestore.payment.domain.DummyAddressFactory;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.payment.domain.DummyPaymentGateway;
import com.learning.ddd.onlinestore.shopping.domain.ShoppingCart;
import com.learning.ddd.onlinestore.transaction.domain.TransactionReceipt;
import com.learning.ddd.onlinestore.transaction.domain.TransactionStatus;

public class CheckoutServiceTest {

	private static final int BOOK_ITEM_ID = 7007;
	private static final int BOOK_ITEM_QUANTITY = 10;
	private final Item BOOK_ITEM = new Item(
		BOOK_ITEM_ID, "Literature", "Book", "SampleBook1", 
		BOOK_ITEM_QUANTITY, 500.0
	);

	private CheckoutService checkoutService;
	private ShoppingCart cart;
	private DomainEventsPublisher domainEventPublisher;
	private DomainEventSubscriber domainEventSubscriber;
	private DomainEventService domainEventService;
	
	@BeforeEach
	void setup() {
		
		// setup CheckoutService with a Test PaymentGateway
		checkoutService = new CheckoutService();
		checkoutService.setPaymentGateway(new DummyPaymentGateway());
		
		// setup Cart
		cart = new ShoppingCart();
		
		domainEventSubscriber = new DummyDomainEventsSubscriber();
		List<DomainEventSubscriber> domainEventSubscribers = new ArrayList<>();
		domainEventSubscribers.add(domainEventSubscriber);
		
		domainEventService = new DomainEventService();
		domainEventService.setDomainEventSubscribers(domainEventSubscribers);
		
		domainEventPublisher = new DummyDomainEventsPublisher();
		domainEventPublisher.setDomainEventService(domainEventService);
		
		cart.setDomainEventPublisher(domainEventPublisher);
	}
	
	@Test
	@DisplayName("Create Order")
	void createOrder() { //ConcludesShopping //CartToOrder (CaterpillarToButterfly)
		
		// setup
		cart.addItem(BOOK_ITEM);
		
		// execute
		Order order = checkoutService.createOrder(
			cart,
			PaymentMethod.CREDIT_CARD,
			DummyAddressFactory.dummyAddress(AddressType.BILLING_ADDRESS),
			DummyAddressFactory.dummyAddress(AddressType.SHIPPING_ADDRESS)
		);
		
		// verify
		assertNotNull(order);
		assertNotNull(order.getItems());
		assertEquals(BOOK_ITEM, order.getItems().get(0));
		assertEquals(BOOK_ITEM_QUANTITY, order.getItems().get(0).getQuantity());
		
	}
	
	@Test
	@DisplayName("Checkout - Consumer confirmed the Order")
	void checkout() {
		
		// setup
		cart.addItem(BOOK_ITEM);
		
		Order order = checkoutService.createOrder(
			cart,
			PaymentMethod.CREDIT_CARD,
			DummyAddressFactory.dummyAddress(AddressType.BILLING_ADDRESS),
			DummyAddressFactory.dummyAddress(AddressType.SHIPPING_ADDRESS)
		);
		
		// execute
		TransactionReceipt txnReceipt = checkoutService.checkout(
			order
		);
		
		// verify
		assertNotNull(txnReceipt);
		assertEquals(TransactionStatus.APPROVED, txnReceipt.getTransactionStatus());
		assertEquals(PaymentMethod.CREDIT_CARD, txnReceipt.getPaymentMethod());
		assertNotNull(txnReceipt.getReceiptInfo());
		assertNotNull(txnReceipt.getMerchantInfo());
		assertNotNull(txnReceipt.getItemInfo());
		
	}

}
