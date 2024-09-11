package com.learning.ddd.onlinestore.order.domain.event.pubsub;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.order.domain.service.OrderService;
import com.learning.ddd.onlinestore.product.application.dto.CheckProductsAvailabilityEvent;
import com.learning.ddd.onlinestore.product.domain.exception.ProductAlreadyExistsException;

//@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class ProductsAvailabilityResponseAsyncConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [order-service] " + ProductsAvailabilityResponseAsyncConsumer.class.getSimpleName();
	
	@Value("${onlinestore.order.events.topic.name:OrderInventoryResponseTopic}")
	private String topicName;
	
	@Autowired
	private OrderService orderService;
	
//	@Autowired
//	private ProductCatalogRepository productcatalogRepository;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[order-service] " + ProductsAvailabilityResponseAsyncConsumer.class.getSimpleName();
	}
	
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	
	@Override
	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException, JMSException, ProductAlreadyExistsException {

		// process async response from inventory-service of checking availability of products in current Order being placed
		
		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.CHECK_PRODUCTS_AVAILABILITY_RESPONSE)) {
		
//			
//			[ order-service / ProductsAvailabilityResponseAsyncConsumer ]
//			
//			if (product not available) { return Order = Rejected }
//
//			/* if (inventory-service not available) { retry 3 times } */ //this is hard to do
//
//			if (product available) {
//				order-service --> payment-service: ASYNC Payment
//				payment-service --> order-service: txn receipt / err
//			}
//			
//			[ order-service ]
//			if (txn failed) { return Order = Rejected }
//			if (payment-service not available) { retry 3 times }
//				
//			if (txn receipt i.e. txn successful) {
//				order-service --> order-service: Confirm Order
//			}
//				
//			order-service --> web-app: Return final status / details of Order
//			
			
			CheckProductsAvailabilityEvent productsAvailabilityEvent =
				(CheckProductsAvailabilityEvent) domainEvent;

			orderService.updateOrderedProductsAvailability(
				productsAvailabilityEvent.getOrderId(), 
				productsAvailabilityEvent.isProductsAvailableInInventory()
			);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Updated availability of ordered Products in Inventory to Order in local data store - "
				+ "orderId = " + productsAvailabilityEvent.getOrderId()
				+ ", product(s) = " + productsAvailabilityEvent.getProducts()
				+ ", ProductsAvailability = " + productsAvailabilityEvent.isProductsAvailableInInventory()
				+ ", event actually represents async request-response between order-service and inventory-service");
			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
//				+ ", latest value of order from DB = " + orderService.getOrder(productsAvailabilityEvent.getOrderId()));		
				
			
//			//	if (product not available) { return Order = Rejected }
//			
//			if (!productsAvailabilityEvent.isProductsAvailableInInventory()) {
//				
//				Order order = orderService.rejectOrder(productsAvailabilityEvent.getOrderId());
//				
//				// IMP to use saveAndFlush() else result in this error - org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing
//				//order = orderRepository.saveAndFlush(order);
//				
//				System.err.println(
//					"OrderService: ProductsAvailabilityResponseAsyncConsumer() - "
//					+ "Order is rejected due to Products not available in Inventory"
//					+ ", Order = " + order
//				);
//				
//				return;
//			}
//			
//			
//			//	if (product available) {
//			//		order-service --> payment-service: ASYNC Payment
//			//		payment-service --> order-service: txn receipt / err
//			//	}
//			//
//			//	if (txn failed) { return Order = Rejected }
//			//	/* if (payment-service not available) { retry 3 times } */ // hard to do
//			//							
//			//	if (txn receipt received i.e. txn successful) {
//			//		order-service --> order-service: Confirm Order
//			//	}			
//			
//			OrderTransaction orderTransaction = orderService.doPayment(productsAvailabilityEvent.getOrderId());
//			// OrderTransaction orderTransaction = paymentGateway.doPayment(updatedOrder);
//			
//			// if all goes well, confirm the order
//			
//			updatedOrder = confirmOrder(cartInfo, updatedOrder, orderTransaction);
			
			
		}
		
	}
	
	
//		
//		order-service --> order-service: Place Order
//		<< code executing in main thread >>
//		
//		order-service --> inventory-service: ASYNC Prod Avail
//		inventory-service --> order-service: prod avail status
//		<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//		
//		if (product not available) { return Order = Rejected }
//		<< code executing in main thread >>
//		
//		/* if (inventory-service not available) { retry 3 times } */ //this is hard to do
//		<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//		<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//		<< main thread spawns Thread1... main thread waits till Thread1 is over >>		
//		
//		if (product available) {
//			order-service --> payment-service: ASYNC Payment
//			payment-service --> order-service: txn receipt / err
//		}
//		<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//		
//		[ order-service ]
//		if (txn failed) { return Order = Rejected }
//		<< code executing in main thread >>
//		
//		if (payment-service not available) { retry 3 times }
//		<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//		<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//		<< main thread spawns Thread2... main thread waits till Thread2 is over >>				
//			
//		if (txn receipt i.e. txn successful) {
//			order-service --> order-service: Confirm Order
//		}
//		<< code executing in main thread >>
//			
//		order-service --> web-app: Return final status / details of Order
//		<< code executing in main thread >>
//	
	
}

