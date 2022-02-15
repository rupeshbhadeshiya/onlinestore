package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEventData;
import com.learning.ddd.onlinestore.cart.domain.event.ItemAddedToCartEventData;
import com.learning.ddd.onlinestore.cart.domain.event.ItemRemovedFromCartEventData;
import com.learning.ddd.onlinestore.commons.util.ItemConversionUtil;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.exception.ItemAlreadyExistsException;

@Component
public class CartEventsConsumer extends DomainEventsConsumer {
	
	public static final String SERVICE_COMPONENT = "==== [inventory-service] " + CartEventsConsumer.class.getSimpleName();
	
	@Autowired
	private Inventory inventory;
	
	
	@Value("${onlinestore.cart.events.topic.name:CartEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[inventory-service] " + CartEventsConsumer.class.getSimpleName();
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
		
		
		// [ Cases for which items needed to be REMOVED from Inventory ] //
		//	1. Items shopped to a Cart
		
		if (domainEvent.getEventName().equals(DomainEventName.ITEM_ADDED_TO_CART)) {
			
			ItemAddedToCartEventData eventData = (ItemAddedToCartEventData) domainEvent.getEventData();
			
			Cart cart = eventData.getCart();
		
			for (CartItem cartItem : cart.getItems()) {
				for (InventoryItem inventoryItem : inventory.getItems()) {
					if (inventoryItem.getName().equals(cartItem.getName())) {
						
						inventory.removeItem(inventoryItem);
						
						System.out.println(
							SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_ADDED_TO_CART.name()
							+ ", Removed corresponding item from Inventory - " + inventoryItem );
					}
				}
			}
		
		// [ Cases for which items needed to be ADDED back to Inventory ] //
		//	1. A Item is removed from a Cart
		//	2. A Cart is emptied
		
		} else if (domainEvent.getEventName().equals(DomainEventName.ITEM_REMOVED_FROM_CART)) {
			
			ItemRemovedFromCartEventData itemRemovedFromCartEventData = (ItemRemovedFromCartEventData) domainEvent.getEventData();
			
			InventoryItem inventoryItem = ItemConversionUtil.fromCartItemToInventoryItem(itemRemovedFromCartEventData.getItem());
			
			try {
				
				inventory.addItem(inventoryItem);
				
				System.out.println(
					SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_REMOVED_FROM_CART.name()
					+ ", Added Item back to Inventory - "
					+ "inventoryItem = " + inventoryItem);
				
			} catch (ItemAlreadyExistsException e) {
				
				System.err.println(
					SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_REMOVED_FROM_CART.name()
					+ ", Error while adding Item back to Inventory - "
					+ "inventoryItem = " + inventoryItem);
				
				e.printStackTrace();
				
				throw new RuntimeException(e);
			}
			
		} else if (domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED_BY_CONSUMER)) {
			
			CartEmptiedEventData cartEmptiedEventData = (CartEmptiedEventData) domainEvent.getEventData();
			
			Cart cart = cartEmptiedEventData.getCart();
			
			for (CartItem cartItem : cart.getItems()) {
			
				InventoryItem inventoryItem = ItemConversionUtil.fromCartItemToInventoryItem(cartItem);
				
				try {
					
					inventory.addItem(inventoryItem);
					
					System.out.println(
						SERVICE_COMPONENT + " - Event: " + DomainEventName.CART_EMPTIED_BY_CONSUMER.name()
						+ ", Added Item back to Inventory - "
						+ "inventoryItem = " + inventoryItem);
					
				} catch (ItemAlreadyExistsException e) {
					
					System.err.println(
						SERVICE_COMPONENT + " - Event: " + DomainEventName.CART_EMPTIED_BY_CONSUMER.name()
						+ ", Error while adding Item back to Inventory - "
						+ "inventoryItem = " + inventoryItem);
					
					e.printStackTrace();
					
					throw new RuntimeException(e);
				}
				
			}
			
		} // else-if
		
	}

}
