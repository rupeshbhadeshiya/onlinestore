package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.product.application.dto.CheckProductsAvailabilityEvent;
import com.learning.ddd.onlinestore.product.domain.exception.ProductAlreadyExistsException;

//@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class ProductsAvailabilityRequestAsyncConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [inventory-service] " + ProductsAvailabilityRequestAsyncConsumer.class.getSimpleName();
	
	@Value("${onlinestore.order.events.topic.name:OrderInventoryRequestTopic}")
	private String topicName;
	
	@Autowired
	private Inventory inventory; 
	
	@Autowired
	private ProductsAvailabilityResponseAsyncProducer 
		ProductsAvailabilityResponseAsyncProducer;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[inventory-service] " + ProductsAvailabilityRequestAsyncConsumer.class.getSimpleName();
	}
	
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	
	@Override
	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException, JMSException, ProductAlreadyExistsException {

		// process async request from order-service of checking availability of products in current Order being placed
		
		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.CHECK_PRODUCTS_AVAILABILITY_REQUEST)) {
			
			CheckProductsAvailabilityEvent request =
				(CheckProductsAvailabilityEvent) domainEvent;

			boolean ProductsAvailability = inventory.checkProductsAvailability(
				request.getProducts()
			);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Received request from order-service to check availability of ordered Products in Inventory - "
				+ "orderId = " + request.getOrderId()
				+ ", ProductsAvailability = " + request.isProductsAvailableInInventory()
				+ ", product(s) = " + request.getProducts()
				+ ", event actually represents async request-response between inventory-service and order-service");
			
			
			CheckProductsAvailabilityEvent response = 
				new CheckProductsAvailabilityEvent(request.getOrderId(), 
					request.getProducts(), ProductsAvailability, 
					OnlinestoreDomainEventName.CHECK_PRODUCTS_AVAILABILITY_RESPONSE
				);
			
			ProductsAvailabilityResponseAsyncProducer.publishDomainEvent(
				response
			);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Responded to order-service with status of availability of ordered Products in Inventory - "
				+ "orderId = " + request.getOrderId()
				+ ", ProductsAvailability = " + request.isProductsAvailableInInventory()
				+ ", product(s) = " + request.getProducts()
				+ ", event actually represents async request-response between inventory-service and order-service");
			
		}
		
	}
	
}

