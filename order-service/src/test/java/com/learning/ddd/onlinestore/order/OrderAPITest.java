package com.learning.ddd.onlinestore.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.commons.util.HttpUtil;
import com.learning.ddd.onlinestore.order.application.dto.CreateOrderDTO;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.AddressType;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderItem;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@TestMethodOrder(OrderAnnotation.class)
class OrderAPITest {

	private static final String CONSUMER_ID = "11";
	private static final int BISCUIT_ITEM_QUANTITY = 2;
	private static final int BATHING_SOAP_ITEM_QUANTITY = 3;
	private final CartItem BISCUIT_ITEM = new CartItem("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final CartItem BATHING_SOAP_ITEM = new CartItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	private final OrderItem BISCUIT_ORDER_ITEM = new OrderItem("Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
	private final OrderItem BATHING_ORDER_SOAP_ITEM = new OrderItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
	
	// Jackson for JSON serialization
	//private ObjectMapper objectMapper = new ObjectMapper(); 
	
	
	@Test
	@org.junit.jupiter.api.Order(1)
	void createOrder() throws Exception {
		
		Cart cart = new Cart(CONSUMER_ID);
		
		cart.addItem(BISCUIT_ITEM);	
		cart.addItem(BATHING_SOAP_ITEM);
		
		PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
		
		Address billingAddress = DummyAddressFactory.dummyAddress(
			AddressType.BILLING_ADDRESS); 
		Address shippingAddress = DummyAddressFactory.dummyAddress(
			AddressType.SHIPPING_ADDRESS);
		
		CreateOrderDTO orderRequestDTO = new CreateOrderDTO(
			CONSUMER_ID,
			cart,
			paymentMethod,
			billingAddress,
			shippingAddress
		);
		
		// execute
		
		Order order = (Order) HttpUtil.post(
			"http://localhost:9030/consumers/"+CONSUMER_ID+"/orders", 
			orderRequestDTO, 
			Order.class
		);
		
		// validate
		
		assertNotNull(order);
		assertEquals(CONSUMER_ID, order.getConsumerId());
		assertTrue(order.getOrderId() > 0);
		assertNotNull(order.getOrderNumber());
		
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, 
				order.getItemCount());
		assertNotNull(order.getItems());
		
//		boolean isBiscuitItemPresent=false, isBathingSoapItemPresent=false;
//		for (OrderItem orderItem : order.getItems()) {
//			if (orderItem.getSubCategory().equals(BISCUIT_ITEM.getSubCategory())) {
//				assertEquals(BISCUIT_ORDER_ITEM, orderItem);
//				isBiscuitItemPresent = true;
//			} else if (orderItem.getSubCategory().equals(BATHING_ORDER_SOAP_ITEM.getSubCategory())) {
//				assertEquals(BATHING_ORDER_SOAP_ITEM, orderItem);
//				isBathingSoapItemPresent = true;
//			}
//		}
//		assertTrue(isBiscuitItemPresent);
//		assertTrue(isBathingSoapItemPresent);
		
		assertTrue(order.getItems().contains(BISCUIT_ORDER_ITEM));
		assertTrue(order.getItems().contains(BATHING_ORDER_SOAP_ITEM));
		
		double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
		assertEquals(expectedAmount, order.getAmount(), 0.0);
		
		assertTrue(paymentMethod.equals(order.getPaymentMethod()));
		assertTrue(billingAddress.equals(order.getBillingAddress()));
		assertTrue(shippingAddress.equals(order.getShippingAddress()));
		
		assertNotNull(order.getTransactions());
		assertTrue(order.getTransactions().size() > 0);
	}
	
	
	@Test
	@org.junit.jupiter.api.Order(2)
	void getAllOrders() throws Exception {
		
		// execute
		
		Order[] orders = (Order[]) HttpUtil.get(
			"http://localhost:9030/consumers/"+CONSUMER_ID+"/orders", 
			Order[].class
		);
		
		// validate
		
		assertNotNull(orders);
		assertTrue(orders.length > 0);
		
		Order order = orders[0];
		assertNotNull(order);
		assertEquals(CONSUMER_ID, order.getConsumerId());
		assertTrue(order.getOrderId() > 0);
		assertNotNull(order.getOrderNumber());
		
		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, order.getItemCount());
		assertNotNull(order.getItems());
		//assertTrue(order.getItems().contains(BISCUIT_ORDER_ITEM));
		//assertTrue(order.getItems().contains(BATHING_ORDER_SOAP_ITEM));
		double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
		assertEquals(expectedAmount, order.getAmount(), 0.0);
		
		assertTrue(PaymentMethod.CREDIT_CARD.equals(order.getPaymentMethod()));
//			assertTrue(billingAddress.equals(order.getBillingAddress()));
//			assertTrue(shippingAddress.equals(order.getShippingAddress()));
//			
		assertNotNull(order.getTransactions());
		assertTrue(order.getTransactions().size() > 0);
	}
	
//	@Test
//	@org.junit.jupiter.api.Order(3)
//	void searchOrders() throws Exception {
//		
//		// execute
//		
//		SearchOrdersDTO searchOrdersDTO = new SearchOrdersDTO();
//		searchOrdersDTO.setSearchText("Parle-G");
//		//searchOrdersDTO.setOrderPlacedDate();
//		
//		
//		Order[] orders = (Order[]) HttpUtil.post(
//			"http://localhost:9030/consumers/"+CONSUMER_ID+"/orders/searches",
//			searchOrdersDTO,
//			Order[].class
//		);
//		
//		// validate
//		
//		assertNotNull(orders);
//		assertTrue(orders.length > 0);
//		
//		Order order = orders[0];
//		assertNotNull(order);
//		assertEquals(CONSUMER_ID, order.getConsumerId());
//		assertTrue(order.getOrderId() > 0);
//		assertNotNull(order.getOrderNumber());
//		
//		assertEquals(BISCUIT_ITEM_QUANTITY + BATHING_SOAP_ITEM_QUANTITY, order.getItemCount());
//		assertNotNull(order.getItems());
//		//assertTrue(order.getItems().contains(BISCUIT_ORDER_ITEM));
//		//assertTrue(order.getItems().contains(BATHING_ORDER_SOAP_ITEM));
//		double expectedAmount = BISCUIT_ITEM.computeAmount() + BATHING_SOAP_ITEM.computeAmount();
//		assertEquals(expectedAmount, order.getAmount(), 0.0);
//		
//		assertTrue(PaymentMethod.CREDIT_CARD.equals(order.getPaymentMethod()));
////			assertTrue(billingAddress.equals(order.getBillingAddress()));
////			assertTrue(shippingAddress.equals(order.getShippingAddress()));
////			
//		assertNotNull(order.getTransactions());
//		assertTrue(order.getTransactions().size() > 0);
//	}
	
	
	@Test
	@org.junit.jupiter.api.Order(4)
	void cancelOrder() throws IOException {

		Order[] orders = (Order[]) HttpUtil.get(
			"http://localhost:9030/consumers/"+CONSUMER_ID+"/orders", 
			Order[].class
		);
		
		assertNotNull(orders);
		assertTrue(orders.length > 0);
		
		Order order = orders[0];
		
		// execute
		HttpUtil.delete(
			"http://localhost:9030/consumers/"+CONSUMER_ID+"/orders"
				+ "?orderNumber=" + order.getOrderNumber() 
			);
		
		// validate
		orders = (Order[]) HttpUtil.get(
			"http://localhost:9030/consumers/"+CONSUMER_ID+"/orders", 
			Order[].class
		);
		assertTrue((orders==null) || (orders.length==0));
	}
	
	
//	@Test
//	void objectToJSONconversion() throws JsonProcessingException {
//		
//		PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
//		System.out.println("\n paymentMethod: " + objectMapper.writeValueAsString(paymentMethod));
//		
//		Address billingAddress = new Address(
//			AddressType.BILLING_ADDRESS, 
//			"l1", "l2", "l3", "l4", 
//			"pincode", "state", "country"
//		);
//		System.out.println("\n billingAddress: " + objectMapper.writeValueAsString(billingAddress));
//		
//		Address shippingAddress = new Address(
//			AddressType.SHIPPING_ADDRESS, 
//			"l1", "l2", "l3", "l4", 
//			"pincode", "state", "country"
//		);
//		System.out.println("\n shippingAddress: " + objectMapper.writeValueAsString(shippingAddress));
//		
//		String CONSUMER_ID = "11";
//		int BISCUIT_ITEM_ID = 1001;
//		int BATHING_SOAP_ITEM_ID = 2002;
//		int BISCUIT_ITEM_QUANTITY = 2;
//		int BATHING_SOAP_ITEM_QUANTITY = 3;
//		CartItem BISCUIT_ITEM = new CartItem(BISCUIT_ITEM_ID, "Grocery", "Biscuit", "Parle-G", BISCUIT_ITEM_QUANTITY, 10.0);
//		CartItem BATHING_SOAP_ITEM = new CartItem(BATHING_SOAP_ITEM_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", BATHING_SOAP_ITEM_QUANTITY, 30.0);
//		
//		Cart sampleCart = new Cart(CONSUMER_ID);
//		
//		sampleCart.addItem(BISCUIT_ITEM);	// establish bi-directional (this is other direction)
//		sampleCart.addItem(BATHING_SOAP_ITEM);	// establish bi-directional (this is other direction)
//		
//		System.out.println("\n Cart: " + objectMapper.writeValueAsString(sampleCart)); // gson.toJson(sampleCart));
//		
//		Order sampleOrder = new Order();
//		
//		sampleOrder.setConsumerId(sampleCart.getConsumerId());
//		sampleOrder.setItems( sampleConvertCartItemsToOrderItems(sampleCart, sampleOrder) );
//		sampleOrder.setItemCount(sampleCart.getItemCount());
//		sampleOrder.setAmount(sampleCart.computeAmount());
//		sampleOrder.setPaymentMethod(paymentMethod);
//		sampleOrder.setBillingAddress(billingAddress);
//		sampleOrder.setShippingAddress(shippingAddress);
//		
//		System.out.println("\n Order: " + objectMapper.writeValueAsString(sampleOrder)); // gson.toJson(sampleOrder));
//		
//	}
//	
//	private List<OrderItem> sampleConvertCartItemsToOrderItems(
//			Cart cart, Order order) {
//		
//		List<OrderItem> orderItems = new ArrayList<OrderItem>();
//		for (CartItem cartItem : cart.getItems()) {
//			OrderItem orderItem = new OrderItem(
//				cartItem.getCategory(), 
//				cartItem.getSubCategory(), 
//				cartItem.getName(), 
//				cartItem.getQuantity(), 
//				cartItem.getPrice()
//			);
//			orderItem.setOrder(order);// set bi-direction (OrderItem -> Order)
//			orderItems.add(orderItem);// set bi-direction (Order -> OrderItem)
//		}
//		
//		return orderItems;
//	}

}
