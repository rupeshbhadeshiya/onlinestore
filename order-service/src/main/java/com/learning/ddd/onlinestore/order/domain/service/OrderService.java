package com.learning.ddd.onlinestore.order.domain.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.domain.CartInfo;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersRequestDTO;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderTransaction;
import com.learning.ddd.onlinestore.order.domain.event.OrderCancelledEvent;
import com.learning.ddd.onlinestore.order.domain.event.OrderConfirmedEvent;
import com.learning.ddd.onlinestore.order.domain.event.pubsub.OrderEventsProducer;
import com.learning.ddd.onlinestore.order.domain.event.pubsub.ProductsAvailabilityRequestAsyncProducer;
import com.learning.ddd.onlinestore.order.domain.repository.OrderRepository;
import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.product.application.dto.CheckProductsAvailabilityEvent;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private PaymentGateway paymentGateway;

	@Autowired
	private OrderEventsProducer orderEventsProducer;
	
	@Autowired
	private ProductsAvailabilityRequestAsyncProducer orderInventoryEventsProducer;	
	
	@Autowired
	private ProcessOrderOrchaestratorSaga processOrderOrchaestratorSaga;

//	................................................
//	
//	web-app --> order-service: checkout
//
//	order-service --> order-service: Place Order
//
//	order-service --> inv-service: ASYNC Prod Avail
//	inv-service --> order-service: prod avail status
//
//	[ order-service ]
//
//	if (product not available) return Order = Rejected
//	if (inv-service not available) retry 3 times...
//
//	if (product available) go ahead...
//	order-service --> pmt-service: ASYNC Payment
//	pmt-service --> order-service: txn receipt / err
//
//	[ order-service ]
//
//	if (txn failed) return Order = Rejected
//	if (pmt-service not available) retry 3 times...
//
//	if (txn receipt i.e. txn successful) go ahead...
//	order-service --> order-service: Confirm Order
//
//	order-service --> web-app: Order status / details
//
//	................................................
	
	public Order processOrder(CartInfo cartInfo, PaymentMethod paymentMethod,
				Address billingAddress, Address shippingAddress) throws JMSException {

		return processOrderOrchaestratorSaga.executeProcessOrderSaga(
					cartInfo, paymentMethod, billingAddress, shippingAddress
			);
	}
	
	
//	................................................	
//
//
//
//	................................................	
	
	//@Transactional
	public Order processOrder2__TO_DELETE__(CartInfo cartInfo, PaymentMethod paymentMethod,
				Address billingAddress, Address shippingAddress) throws JMSException {
		
		final Order order = placeOrder(cartInfo, paymentMethod, billingAddress, shippingAddress);

		
		checkProductAvailabilityWithInventory(cartInfo, order);
		
		
		// theMonitorForCommunicationBetweenThreads
		class ProductsAvailability {
			boolean availability = false;
			public boolean isAvailability() { return availability; }
			public void setAvailability(boolean availability) { this.availability = availability; }
		}
		final ProductsAvailability ProductsAvailability = new ProductsAvailability();
		
//		final List<Boolean> areProductsAvailableInInventory = new ArrayList<>();
//		areProductsAvailableInInventory.add(false);
		
		// ... wait this thread till we get a response from inventory-service
//		try {
//			System.out.println("***************** Entering in waiting state ...");
//			
//			ProductsAvailability.wait();
//			
//			System.out.println("***************** Awaken from waiting state ...");
//			
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		
		// check if a response is received from inventory-service then awake/notify this thread
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 5;
				while ( 
					// wait till 5 seconds are not over
					(i > 0)
					
					&& 
					
					// and, mainly, wait till Products availability from Inventory is not known
					!orderRepository.existsProductsAvailableInInventory(order.getOrderId())		
				){
					
					System.out.println("~~~~~~~~~~ Remaining: " + i + " seconds");
					
					try {
						i--;
					   	Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
					} catch (InterruptedException e) {
						e.printStackTrace();	
						//I don't think you need to do anything for your particular problem
					}
				}
				
				System.out.println("~~~~~~~~~~ Job done, notifying waiting threads to wake up");
				ProductsAvailability.setAvailability(true);
//				ProductsAvailability.notify();
				System.out.println("~~~~~~~~~~ notified waiting threads to wake up");
				
			}
			
		}).start();
		
		
		
		while (!ProductsAvailability.isAvailability()) {
			// just wait...
		}
		
		
		//Order updatedOrder = orderRepository.findById(order.getOrderId()).get();
		
		Order updatedOrder = order;
		updatedOrder.setProductsAvailableInInventory(true);
		
		
		// ... if Products availability from Inventory is still now known 
		// then reject the Order and return with error
		// [IMP] first load updated Order from DB as above aync communication with inventory
		// would have resulted in update in Order about product availability
		// so it is very important to retrieve updated Order from DB

//		Order updatedOrder = orderRepository.findById(order.getOrderId()).get();
//		System.out.println("~~~~~~~~~~~~ updated Order = " + updatedOrder);
//		if (!updatedOrder.isProductsAvailableInInventory()) {
		
		if (!orderRepository.existsProductsAvailableInInventory(order.getOrderId())) {
			
			updatedOrder = rejectOrder(cartInfo, updatedOrder);
			
			return updatedOrder;
		}
		
		
		// ... reaching here means the ordered Products are available in Inventory,
		// so now process the Payment
		
		OrderTransaction orderTransaction = paymentGateway.doPayment(updatedOrder);
		
		// if all goes well, confirm the order
		
		updatedOrder = confirmOrder(cartInfo, updatedOrder, orderTransaction);
		
		
		return updatedOrder;
	}


	private void checkProductAvailabilityWithInventory(CartInfo cartInfo, Order order) throws JMSException {
		// first check inventory-service has items in order, otherwise reject Order
		
		CheckProductsAvailabilityEvent checkProductsAvailabilityEvent
			= new CheckProductsAvailabilityEvent(order.getOrderId(), cartInfo.getProducts(), 
				OnlinestoreDomainEventName.CHECK_PRODUCTS_AVAILABILITY_REQUEST
			);
		orderInventoryEventsProducer.publishDomainEvent(checkProductsAvailabilityEvent);
		
		
//		// ... wait while we get response from inventory-service
//		
//		int i = 5;
//		while ( 
//			// wait till 5 seconds are not over
//			(i > 0)
//			
//			&& 
//			
//			// and, mainly, wait till Products availability from Inventory is not known
//			!orderRepository.isProductsAvailableInInventory(order.getOrderId())		
//		){
//			// System.out.println("Remaining: " + i + " seconds");
//			try {
//				i--;
//			   	Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
//			} catch (InterruptedException e) {
//				//I don't think you need to do anything for your particular problem
//			}
//		}
	}
	
	
	private Order placeOrder(CartInfo cartInfo, PaymentMethod paymentMethod, Address billingAddress,
			Address shippingAddress) {
		
		System.out.println(
			"OrderService: createOrderAndProcessPayment() - started; "
			+ "cartInfo="+cartInfo+", paymentMethod="+paymentMethod
			+ ", billingAddress="+billingAddress+", shippingAddress="+shippingAddress);
		
		Order order = new Order(cartInfo);
		order = orderRepository.save(order);
		
		System.out.println(
			"OrderService: createOrderAndProcessPayment() - Order placed; "
			+ "order = " + order + ", cartInfo = " + cartInfo);
		
		return order;
	}

	private Order confirmOrder(CartInfo cartInfo, Order order, OrderTransaction orderTransaction)
			throws JMSException {
		
		// on successful payment, add transaction receipt to the order
		order.addTransaction(orderTransaction);
		
		// and mark the order as confirmed
		order.confirmOrder();
		
		// ... and persist the Order and Transactions within Order
		// IMP to use saveAndFlush() else result in this error - org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing
		//order = orderRepository.saveAndFlush(order);
		order = orderRepository.save(order);
		
		System.out.println(
			"OrderService: createOrderAndProcessPayment() - Order confirmed; "
			+ "order = " + order + ", cartInfo = " + cartInfo);

		// ... and publish the change as a domain event
		
		OrderConfirmedEvent event = new OrderConfirmedEvent(order.getOrderInfo(), cartInfo);
		orderEventsProducer.publishDomainEvent(event);
		
		return order;
	}

	private Order rejectOrder(CartInfo cartInfo, Order order) {
		
		order.rejectOrder();
		
		// IMP to use saveAndFlush() else result in this error - org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing
		//order = orderRepository.saveAndFlush(order);
		order = orderRepository.save(order);
		
		System.err.println(
			"OrderService: createOrderAndProcessPayment() - Order rejected; "
			+ "order = " + order + ", cartInfo = " + cartInfo);
		
		return order;
	}

	
//	@Transactional
//	public Order placeOrder(CartInfo cartInfo, PaymentMethod paymentMethod,
//			Address billingAddress, Address shippingAddress) 
//					throws JMSException {
//		
//		System.out.println(
//			"OrderService: createOrderAndProcessPayment() - started; "
//			+ "cartInfo="+cartInfo+", paymentMethod="+paymentMethod
//			+ ", billingAddress="+billingAddress+", shippingAddress="+shippingAddress);
//		
//		Order order = new Order(cartInfo);
//		order = orderRepository.save(order);
//		
//		System.out.println(
//			"OrderService: createOrderAndProcessPayment() - Order placed; "
//			+ "order="+order
//			+ "cartInfo="+cartInfo);
//
//		
//		// first check inventory-service has items in order, otherwise reject Order
//		
//		CheckProductsAvailabilityEvent checkProductsAvailabilityEvent
//			= new CheckProductsAvailabilityEvent(order.getOrderId(), cartInfo.getProducts(), 
//				OnlinestoreDomainEventName.CHECK_PRODUCTS_AVAILABILITY_REQUEST
//			);
//		orderInventoryEventsProducer.publishDomainEvent(checkProductsAvailabilityEvent);
//		
//		
//		// ... wait while we get response from inventory-service
//		
//		int i = 5;
//		while ( 
//			// wait till 5 seconds are not over
//			(i > 0)
//			
//			&& 
//			
//			// and, mainly, wait till Products availability from Inventory is not known
//			!orderRepository.isProductsAvailableInInventory(order.getOrderId())		
//		){
//			// System.out.println("Remaining: " + i + " seconds");
//			try {
//				i--;
//			   	Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
//			} catch (InterruptedException e) {
//				//I don't think you need to do anything for your particular problem
//			}
//		}
//		
//		// ... if Products availability from Inventory is still now known 
//		// then reject the Order and return with error
//		// [IMP] first load updated Order from DB as above aync communication with inventory
//		// would have resulted in update in Order about product availability
//		// so it is very important to retrieve updated Order from DB
//		order = orderRepository.findById(order.getOrderId()).get();
//		
//		if (!order.isProductsAvailableInInventory()) {
//	
//			order.rejectOrder();
//			order = orderRepository.save(order);
//
//			System.err.println(
//				"OrderService: createOrderAndProcessPayment() - Order rejected; "
//				+ "order="+order
//				+ "cartInfo="+cartInfo);
//	
//			return order;
//		}
//		
//		
//		// ... reaching here means the ordered Products are available in Inventory,
//		// so now process the Payment
//		
//		OrderTransaction transactionReceipt = paymentGateway.doPayment(order);
//		
//		// on successful payment, add transaction receipt to the order
//		order.addTransaction(transactionReceipt);
//		
//		// and mark the order as confirmed
//		order.confirmOrder();
//		
//		// ... and persist the Order and Transactions within Order
//		order = orderRepository.save(order);
//		
//		System.out.println(
//			"OrderService: createOrderAndProcessPayment() - Order confirmed; "
//			+ "order="+order
//			+ "cartInfo="+cartInfo);
//
//		// ... and publish the change as a domain event
//		
//		OrderConfirmedEvent event = new OrderConfirmedEvent(order.getOrderInfo(), cartInfo);
//		orderEventsProducer.publishDomainEvent(event);
//		
//		return order;
//	}

	public List<Order> getOrders(String consumerId) {
		return orderRepository.findByConsumerIdOrderByCreationDateDesc(consumerId);
	}
	
	public Order getOrder(int orderId) {
		return orderRepository.findById(orderId).get();
	}
	
//	public List<Order> searchOrders(String consumerId, String searchText, Date orderPlacedDate) {
//
//		Object[] objects = orderRepository.findBySearchCriteria(consumerId, searchText, orderPlacedDate);
//		System.out.println(Arrays.asList(objects));
//		return new ArrayList<>();
//	}
	
	public List<Order> searchOrders(SearchOrdersRequestDTO searchOrdersRequestDTO) throws ParseException {
		
		Date purchaseDate = null;
		try { 
			purchaseDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").parse(searchOrdersRequestDTO.getPurchaseDate());	
		} catch (ParseException e) {
			purchaseDate = new Date();	
		}
		
		PaymentMethod paymentMethod = null;
		if ( (searchOrdersRequestDTO.getPaymentMethod() != null) 
				&& !searchOrdersRequestDTO.getPaymentMethod().isEmpty() ) {
			
			PaymentMethod.valueOf(searchOrdersRequestDTO.getPaymentMethod()); 
		}
		
		return orderRepository.findByExample(
			searchOrdersRequestDTO.getOrderNumber(),
			searchOrdersRequestDTO.getTransactionNumber(),
			purchaseDate,
			paymentMethod,
			searchOrdersRequestDTO.getItemDetails(),
			searchOrdersRequestDTO.getAddressDetails()
		);
		
	}

	@Transactional
	public void cancelOrder(String consumerId, String orderNumber) throws CloneNotSupportedException, JMSException {
		
		System.out.println(
			"OrderService: cancelOrder() - Going to cancel Order; "
			+ "consumerId="+consumerId
			+ "orderNumber="+orderNumber);
		
		Order order = orderRepository.findByOrderNumber(orderNumber);
		Order copyOfOrderToBeCancelled = order.clone();
		
		System.out.println("--------- cancelOrder(orderNumber): order = " + order + " -----------");
		
		orderRepository.deleteByConsumerIdAndOrderNumber(consumerId, orderNumber);
		
		System.out.println(
			"OrderService: cancelOrder() - Cancelled the Order; "
			+ "consumerId="+consumerId
			+ "orderNumber="+orderNumber);
		
		OrderCancelledEvent event = new OrderCancelledEvent(copyOfOrderToBeCancelled.getOrderInfo());
		orderEventsProducer.publishDomainEvent(event);
	}
	
	@Transactional
	public void cancelOrder(String consumerId, int orderId) throws CloneNotSupportedException, JMSException {
		
		System.out.println("--------- cancelOrder(orderId): orderId = " + orderId + " -----------");
		
		Order order = orderRepository.findById(orderId).get();
		
		System.out.println("--------- cancelOrder(orderId): order = " + order + " -----------");
		
		Order copyOfOrderToBeCancelled = order.clone();
		
		System.out.println("--------- cancelOrder(orderNumber): order = " + order + " -----------");
		
		orderRepository.deleteById(orderId);
		
		OrderCancelledEvent event = new OrderCancelledEvent(copyOfOrderToBeCancelled.getOrderInfo());
		orderEventsProducer.publishDomainEvent(event);
	}

	@Transactional
	public void updateOrderedProductsAvailability(int orderId, boolean productsAvailableInInventory) {
		
		orderRepository.updateOrderedProductsAvailability(orderId, productsAvailableInInventory);
		
//		Order order = orderRepository.findById(orderId).get();
//		order.setProductsAvailableInInventory(productsAvailableInInventory);
//		orderRepository.saveAndFlush(order);

		System.out.println("updateOrderedProductsAvailability(): updated Products availability"
			+ " outcome from inventory-service"
			+ ", orderId = " + orderId 
			+ ", productsAvailableInInventory = " + productsAvailableInInventory);
	}


}
