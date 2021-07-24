package com.learning.ddd.onlinestore.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.service.OrderService;

@EnableEurekaClient
@RestController
@RequestMapping("/consumers")
@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication //(scanBasePackages = {"com.learning.ddd.*"})
public class OrderServiceBootApp extends SpringBootServletInitializer {

	@Autowired
	private OrderService orderService;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OrderServiceBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceBootApp.class, args);
	}
	
	@PostMapping("/{consumerId}/orders")
	public ResponseEntity<Order> createOrder(
			@PathVariable String consumerId,
			@RequestBody CreateOrderDTO createOrderDTO) {
		
		Order order = orderService.createOrderAndProcessPayment(
				createOrderDTO.getCart(), 
				createOrderDTO.getPaymentMethod(), 
				createOrderDTO.getBillingAddress(), 
				createOrderDTO.getShippingAddress()
			);
		
		return new ResponseEntity<Order>(order, HttpStatus.CREATED);
	}
	
	@GetMapping("/{consumerId}/orders")
	public ResponseEntity<List<Order>> getAllOrders(@PathVariable String consumerId) {
		
		List<Order> orders = orderService.getOrders(consumerId);
		
		return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
	}
	
//	@PostMapping("/{consumerId}/orders/searches")
//	public ResponseEntity<List<Order>> searchOrders(
//			@PathVariable String consumerId,
//			@RequestBody SearchOrdersDTO searchOrdersDTO) {
//
//		List<Order> orders = orderService.searchOrders(
//			consumerId,
//			searchOrdersDTO.getSearchText(),
//			searchOrdersDTO.getOrderPlacedDate());
//		
//		return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
//	}
		
	@DeleteMapping("/{consumerId}/orders/{orderNumber}")
	public ResponseEntity<?> deleteOrder(
			@PathVariable String consumerId,
			@PathVariable String orderNumber) {
		
		orderService.deleteOrder(consumerId, orderNumber);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
//	@GetMapping("/{consumerId}/orders")
//	public ResponseEntity<List<Order>> getAllOrders(@PathVariable String consumerId) {
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
//		PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
//		
//		Address billingAddress = new Address(
//			AddressType.BILLING_ADDRESS, 
//			"l1", "l2", "l3", "l4", 
//			"pincode", "state", "country"
//		);
//		Address shippingAddress = new Address(
//			AddressType.SHIPPING_ADDRESS, 
//			"l1", "l2", "l3", "l4", 
//			"pincode", "state", "country"
//		);
//		
//		Order sampleOrder = new Order();
//		
//		sampleOrder.setOrderId(1234);
//		sampleOrder.setConsumerId(sampleCart.getConsumerId());
//		sampleOrder.setItems( sampleConvertCartItemsToOrderItems(sampleCart, sampleOrder) );
//		sampleOrder.setItemCount(sampleCart.getItemCount());
//		sampleOrder.setAmount(sampleCart.computeAmount());
//		sampleOrder.setPaymentMethod(paymentMethod);
//		sampleOrder.setBillingAddress(billingAddress);
//		sampleOrder.setShippingAddress(shippingAddress);
//		
//		List<OrderTransaction> transactions = new ArrayList<OrderTransaction>();
//		OrderTransaction t1 = new OrderTransaction();
//		t1.setId(12345);
//		t1.setTransactionNumber(UUID.randomUUID().toString());
//		t1.setTransactionStatus(TransactionStatus.APPROVED);
//		t1.setItems( sampleConvertCartItemsToOrderItems(sampleCart, sampleOrder) );
//		t1.setPaymentMethod(sampleOrder.getPaymentMethod());
//		t1.setPurchaseDate(new Date());
//		t1.setTotalItems(sampleOrder.getItemCount());
//		t1.setTotalAmount(sampleOrder.getAmount());
//		MerchantInfo merchantInfo = new MerchantInfo("TestMerchant","123-456-789",
//			"test@email.com", "adrsLine1","adrsLine2","adrsLine3");
//		t1.setMerchantInfo(merchantInfo);
//		transactions.add(t1);
//		sampleOrder.setTransactions(transactions);
//		
//		List<Order> orders = new ArrayList<>();
//		orders.add(sampleOrder);
//		
//		return orders;
//	}
	
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

