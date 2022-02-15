package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.commons.util.ItemConversionUtil;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.exception.ItemAlreadyExistsException;
import com.learning.ddd.onlinestore.order.domain.OrderItem;
import com.learning.ddd.onlinestore.order.domain.event.OrderCancelledEventData;

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
	protected void consumeDomainEvent(DomainEvent domainEvent) throws CloneNotSupportedException, JMSException {

		// FIXME Inefficient code - figure out if there is any way
		//			by which Inventory and Cart can use same Item class?
		//			+ can Order also use same Item class?
		//
		// FIXME Why we need 3 different classes carrying same data?
		//			-> InventoryItem, CartItem
		
		// [ Cases for which items needed to be ADDED back to Inventory ] //
		//	1. An Order is cancelled		
		
		if (domainEvent.getEventName().equals(DomainEventName.ORDER_CANCELLED)) {
			
			OrderCancelledEventData orderCancelledEventData = (OrderCancelledEventData) domainEvent.getEventData();
			
			for (OrderItem orderItem : orderCancelledEventData.getOrder().getItems()) {
				
				InventoryItem inventoryItem = ItemConversionUtil.fromOrderItemToInventoryItem(orderItem);
				
				try {
					
					inventory.addItem(inventoryItem);
					
					System.out.println(
						SERVICE_COMPONENT + " - Event: " + DomainEventName.ORDER_CANCELLED.name()
						+ ", Added Item back to Inventory - "
						+ "inventoryItem = " + inventoryItem);
					
				} catch (ItemAlreadyExistsException e) {
					
					System.err.println(
						SERVICE_COMPONENT + " - [inventory-service] OrderEventsConsumer - "
						+ "Event: " + DomainEventName.ORDER_CANCELLED.name()
						+ ", Error while adding Item back to Inventory - "
						+ "inventoryItem = " + inventoryItem);
					
					e.printStackTrace();
					
					throw new RuntimeException(e);
				}
				
			} // for loop ends
		} // if conditions ends
	} // method ends

}