package com.learning.ddd.onlinestore.order.domain.service;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.learning.ddd.onlinestore.cart.domain.CartInfo;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsPublisher;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderTransaction;
import com.learning.ddd.onlinestore.order.domain.TransactionStatus;
import com.learning.ddd.onlinestore.order.domain.event.OrderConfirmedEvent;
import com.learning.ddd.onlinestore.order.domain.event.pubsub.OrderEventsProducer;
import com.learning.ddd.onlinestore.order.domain.event.pubsub.ProductsAvailabilityRequestAsyncProducer;
import com.learning.ddd.onlinestore.order.domain.repository.OrderRepository;
import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.product.application.dto.CheckProductsAvailabilityEvent;


//
//order-service --> order-service: Place Order
//<< code executing in main thread >>
//
//order-service --> inventory-service: ASYNC Prod Avail
//inventory-service --> order-service: prod avail status
//<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//
//if (product not available) { return Order = Rejected }
//<< code executing in main thread >>
//
///* if (inventory-service not available) { retry 3 times } */ //this is hard to do
//<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//<< main thread spawns Thread1... main thread waits till Thread1 is over >>		
//
//if (product available) {
//	order-service --> payment-service: ASYNC Payment
//	payment-service --> order-service: txn receipt / err
//}
//<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//
//[ order-service ]
//if (txn failed) { return Order = Rejected }
//<< code executing in main thread >>
//
//if (payment-service not available) { retry 3 times }
//<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//<< main thread spawns Thread2... main thread waits till Thread2 is over >>				
//	
//if (txn receipt i.e. txn successful) {
//	order-service --> order-service: Confirm Order
//}
//<< code executing in main thread >>
//	
//order-service --> web-app: Return final status / details of Order
//<< code executing in main thread >>
//

@Component
public class ProcessOrderOrchaestratorSaga {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderEventsProducer orderEventsProducer;

	@Autowired
	private PaymentGateway paymentGateway;
	
	@Autowired
	private ProductsAvailabilityRequestAsyncProducer orderInventoryEventsProducer;
	
	// this variable helps to record Order status/details in-memory and share among methods of this class
	private Order order;
	

	// below methods are created so as that this class's variables can be used inside Thread's run implementation
	protected DomainEventsPublisher getOrderInventoryEventsProducer() {
		return orderInventoryEventsProducer;
	}
	protected Order getOrder() {
		return order;
	}
	
	
	
	//@org.springframework.transaction.annotation.Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Order executeProcessOrderSaga(CartInfo cartInfo, PaymentMethod paymentMethod,
			Address billingAddress, Address shippingAddress) throws JMSException {
		
		
		placeOrder(cartInfo, paymentMethod, billingAddress, shippingAddress);	
		
		
		asyncEnquiryToInventoryForProductsAvailability(cartInfo);		
		
		
		if (checkIfProductsAvailabilityIsReceived(cartInfo)) {
			
			//System.out.println("%%%%%%%%%%%%%% OrderService: executeProcessOrderSaga() - Products Available = true! "
			//	+ " going for payment processing and confirming the order");
			
			processPaymentAndConfirmOrder(cartInfo);

			
		} else {
			
			//System.out.println("%%%%%%%%%%%%%% OrderService: executeProcessOrderSaga() - Products Available = false! "
			//	+ " going for rejecting the order");
			
			rejectOrder();
			
		}
		
		return order;
	}


	@Transactional
	private void placeOrder(CartInfo cartInfo, PaymentMethod paymentMethod, Address billingAddress,
			Address shippingAddress) {
		
		//System.out.println(
		//	"OrderService: executeProcessOrderSaga() - started; "
		//	+ "cartInfo="+cartInfo+", paymentMethod="+paymentMethod
		//	+ ", billingAddress="+billingAddress+", shippingAddress="+shippingAddress);
		
		order = new Order(cartInfo);
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		
		order = orderRepository.saveAndFlush(order);
		
		System.out.println("OrderService: executeProcessOrderSaga() - Order placed; "
				+ "order="+order + "cartInfo="+cartInfo);	
		
		//System.out.println("OrderService: executeProcessOrderSaga() - Retrieving Order from DB; "
		//		+ "order="+orderRepository.findById(order.getOrderId()));
	}
	
	
	private void asyncEnquiryToInventoryForProductsAvailability(CartInfo cartInfo) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				// asynchronously check ordered products availability from inventory-service
				
				CheckProductsAvailabilityEvent checkProductsAvailabilityEvent = 
					new CheckProductsAvailabilityEvent(getOrder().getOrderId(), cartInfo.getProducts(), 
						OnlinestoreDomainEventName.CHECK_PRODUCTS_AVAILABILITY_REQUEST
					);
				
				try {
					getOrderInventoryEventsProducer().publishDomainEvent(checkProductsAvailabilityEvent);
				} catch (JMSException e) {
					System.err.println(
						"OrderService: executeProcessOrderSaga() - JMSException while publishing async request"
						+ " to inventory-service to check for availability of ordered Products; "
						+ "order = "+order + ", cartInfo = "+cartInfo);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	@org.springframework.transaction.annotation.Transactional(isolation = Isolation.READ_UNCOMMITTED)
	private Boolean checkIfProductsAvailabilityIsReceived(CartInfo cartInfo) {
		
		//System.out.println("=====~~~~~ OrderService: executeProcessOrderSaga() - after sleep,"
		//	+ " latest value of order from DB = " +  orderRepository.findById(order.getOrderId()).get());
		
		for (int i = 1; i <= 5; i++) {
			
			if (orderRepository.existsProductsAvailableInInventory(order.getOrderId())) {
				break;
			}
			
			//System.out.println("----- OrderService: executeProcessOrderSaga() - before sleep, i = " + i);
			try {
				Thread.sleep(100L);
				
			} catch (InterruptedException e) {
				System.err.println(
					"OrderService: executeProcessOrderSaga() - Exception while trying to sleep for 100ms interval"
					+ " while in a loop keep checking if an update on Products availability from inventory-service"
					+ " has been received; "+ "order = "+order + ", cartInfo = "+cartInfo);
				e.printStackTrace();
			}
			
			System.out.println("=====~~~~~ OrderService: executeProcessOrderSaga() - after sleep, i = " + i
				+ ", latest value of order from DB = " +  orderRepository.findById(order.getOrderId()).get());
		}
		
		return orderRepository.existsProductsAvailableInInventory(order.getOrderId());
	}
	
	
	@Transactional
	private void processPaymentAndConfirmOrder(CartInfo cartInfo) throws JMSException {
		
		// first update Products availability to Order in class level field
		// since so far this Order object is existing in memory in Hibernate
		// and I faced lot of problems when trying to get latest from DB, couldn't find
		// 'true' against productsAvailableInInventory field
		// so need to set it here explicitly
		order.setProductsAvailableInInventory(true);
		
		// now process payment and update Transaction against Order
		
		OrderTransaction orderTransaction = null;
		
		final String msg2 = "notifying_piece2";
		synchronized (msg2) {
			
			orderTransaction = paymentGateway.doPayment(order);
			
			//System.out.println(new Date() + ": Main Thread: starting Thread2");
			new Thread(new CustomThread22("Thread2", msg2)).start();
			try {
				//System.out.println(new Date() + ": Main Thread: waiting on msg2 for 100ms");
				msg2.wait(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			//System.out.println(new Date() + ": Main Thread: notified on msg2");
		}
		
		
		// if payment processing is successful, then confirm the Order
		
		if ((orderTransaction != null) 
				&& 
			(
				orderTransaction.getTransactionStatus().equals(TransactionStatus.APPROVED)
				||
				orderTransaction.getTransactionStatus().equals(TransactionStatus.PENDING)
			)) 
		{
			
			
			// 5: if all goes well, then add transaction to order and confirm the order
			
			// on successful payment, add transaction receipt to the order
			order.addTransaction(orderTransaction);
			
			// mark the order as confirmed
			order.confirmOrder();
			
			// ... and persist the Order and Transactions within Order
			// update class level Order variable since it will be returned as response 
			// Note: we use SaveAndFlush here so that order changes are committed immediately 
			order = orderRepository.saveAndFlush(order);
			
			System.out.println(
				"OrderService: executeProcessOrderSaga() - Order confirmed; "
				+ "order = " + order + ", cartInfo = " + cartInfo);
	
			// ... and publish the change as a domain event
			
			OrderConfirmedEvent event = new OrderConfirmedEvent(order.getOrderInfo(), cartInfo);
			orderEventsProducer.publishDomainEvent(event);
			
			
		} else {
			
			// if payment processing failed or transaction was declined, then reject the Order

			rejectOrder();
			
			return;
			
			
		}
		
	}
	
	
	@Transactional
	private void rejectOrder() {
		
		//System.out.println(
		//	"----- OrderService: executeProcessOrderSaga() - Going to check if status of Order says"
		//	+ ", to move forward for Payment to confirm the order, or,"
		//	+ " since items are not available in inventory so reject the order; order = " + order);
		
		if (!orderRepository.existsProductsAvailableInInventory(order.getOrderId())) {
			
			order.rejectOrder();
			
			order = orderRepository.save(order);
			
			System.err.println("OrderService: executeProcessOrderSaga() - Order rejected; order = " + order);
		}

	}
	
	
	
	class CustomThread22 implements Runnable {
	
		private String name;
		private String msg2;
	
		public CustomThread22(String name, String msg2) {
			this.name = name;
			this.msg2 = msg2;
		}
		
		public String getName() {
			return name;
		}
		
		public String getMsg2() {
			return msg2;
		}
		
		@Override
		public void run() {
			synchronized (msg2) {
				//System.out.println(new Date() + ": CustomThread22 [name = " + name + ", msg2 = " + msg2 + "] started");
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				msg2.notify();
				//System.out.println(new Date() + ": CustomThread22 [name = " + name + ", msg2 = " + msg2 + "] completed");				
			}
		}
		
	}
	
}
