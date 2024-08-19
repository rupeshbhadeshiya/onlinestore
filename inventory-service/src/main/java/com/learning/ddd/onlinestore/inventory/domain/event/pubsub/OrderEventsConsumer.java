package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.order.application.dto.OrderInfo;
import com.learning.ddd.onlinestore.order.domain.event.OrderCancelledEvent;

@Component
public class OrderEventsConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [inventory-service] " + OrderEventsConsumer.class.getSimpleName();
	
	@Autowired
	private Inventory inventory;
	
	
	@Value("${onlinestore.order.events.topic.name:OrderEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[inventory-service] " + OrderEventsConsumer.class.getSimpleName();
	}
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	@Override
	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException, JMSException {

		// FIXME Inefficient code - figure out if there is any way
		//			by which Inventory and Cart can use same Item class?
		//			+ can Order also use same Item class?
		//
		// FIXME Why we need 3 different classes carrying same data?
		//			-> InventoryItem, CartItem
		
		// [ Cases for which items needed to be ADDED back to Inventory ] //
		//	1. An Order is cancelled		
		
		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.ORDER_CANCELLED)) {
			
			OrderCancelledEvent orderCancelledEvent = (OrderCancelledEvent) domainEvent;
			
			OrderInfo OrderInfo = orderCancelledEvent.getOrderInfo();
			
			List<Product> products = OrderInfo.getProducts();
			
			// we don't add InventoryItem if the Order is cancelled
			// but mark it as now available for shopping 
			
			inventory.updateListOfProductsAsAvailableForShopping(products);			
				
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Updated corresponding products in Inventory as now available for shopping"
				+ ", Updated products = " + products);
				
		} // if conditions ends
	} // method ends

}