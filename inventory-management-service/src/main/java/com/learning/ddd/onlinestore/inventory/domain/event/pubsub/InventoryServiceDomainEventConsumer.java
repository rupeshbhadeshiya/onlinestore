package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.commons.util.ItemConversionUtil;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.order.domain.OrderItem;
import com.learning.ddd.onlinestore.order.domain.event.OrderCancelledEvent;

@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class InventoryServiceDomainEventConsumer implements DomainEventConsumer {

	@Autowired
	private Inventory inventory;
	
	@Override
	public void consumeEvent(DomainEvent domainEvent) {

		System.out.println("==== InventoryServiceDomainEventConsumer "
			+ "- processing event - " + domainEvent);
		
		// FIXME Inefficient code - figure out if there is any way
		//			by which Inventory and Cart can use same Item class?
		//			+ can Order also use same Item class?
		//
		// FIXME Why we need 3 different classes carrying same data?
		//			-> InventoryItem, CartItem, OrderItem???
		
		// [ Cases for which items needed to be REMOVED from Inventory ] //
		//	1. Items shopped to a Cart
		
		if ( (domainEvent.getEventName().equals(DomainEventName.CART_PULLED))
				|| (domainEvent.getEventName().equals(DomainEventName.CART_UPDATED)) ) {
			Cart cart = (Cart) domainEvent.getEventData();
			for (CartItem cartItem : cart.getItems()) {
				for (InventoryItem inventoryItem : inventory.getItems()) {
					if (inventoryItem.getName().equals(cartItem.getName())) {
						inventory.removeItem(inventoryItem);
						System.out.println("==== InventoryServiceDomainEventConsumer - "
							+ "Due to Cart Pulled/Updated - REMOVED item from Inventory - " + inventoryItem);
					}
				}
			}
		}
		
		// [ Cases for which items needed to be ADDED back to Inventory ] //
		//	1. A Cart is emptied
		//	2. An Order is cancelled			
		
		else if (domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED)) {
			CartEmptiedEvent cartEmptiedEvent = (CartEmptiedEvent) domainEvent;
			for (CartItem cartItem : cartEmptiedEvent.getCartItemsBeingReleased()) {
				InventoryItem inventoryItem = ItemConversionUtil.fromCartItemToInventoryItem(cartItem);
				inventory.addItem(inventoryItem);
						System.out.println("==== InventoryServiceDomainEventConsumer - "
							+ "Due to Cart Emptied - ADDED item to Inventory - " + inventoryItem);
			}
			
		} else if (domainEvent.getEventName().equals(DomainEventName.ORDER_CANCELLED)) {
			OrderCancelledEvent orderCancelledEvent = (OrderCancelledEvent) domainEvent;
			for (OrderItem orderItem : orderCancelledEvent.getOrderItems()) {
				InventoryItem inventoryItem = ItemConversionUtil.fromOrderItemToInventoryItem(orderItem);
				inventory.addItem(inventoryItem);
						System.out.println("==== InventoryServiceDomainEventConsumer - "
							+ "Due to Order Cancelled - ADDED item to Inventory - " + inventoryItem);
			}
		}
			
	}
	
	
	
//	private Inventory inventory; // autowiring on setter
//	
//	@Autowired
//	private DomainEventsFacilitator domainEventsFacilitator;
//
//	
//	
//	
//	@Autowired
//	public void setInventory(Inventory inventory) {
//		this.inventory = inventory;
//		
//		List<DomainEventName> interestedDomainEvents = Arrays.asList(
//			new DomainEventName[] {
//				DomainEventName.CART_PULLED,
//				DomainEventName.CART_UPDATED,
//				DomainEventName.CART_RELEASED
//			}
//		);
//		
//		domainEventsFacilitator.subscribeToDomainEvents(interestedDomainEvents, this);
//		
//		System.out.println("==== InventoryServiceDomainEventConsumer "
//			+ "- subscribed itself to DomainEventsFacilitator for events - " 
//			+ interestedDomainEvents);
//		
//	}
//	
//	@Override
//	public void processEvent(DomainEvent domainEvent) {
//		
//		System.out.println("==== InventoryServiceDomainEventConsumer "
//				+ "- processing event - " + domainEvent);
//		
//		// FIXME Inefficient code - figure out if there is any way
//		//			by which Inventory and Cart can use same Item class?
//		//			+ can Order also use same Item class?
//		//
//		// FIXME Why we need 3 different classes carrying same data?
//		//			-> InventoryItem, CartItem, OrderItem???
//		
//		// Remove all those items which are added to a Cart
//		if (domainEvent.getEventName() == DomainEventName.CART_PULLED) {
//			Cart cart = (Cart) domainEvent.getEventData();
//			for (CartItem cartItem : cart.getItems()) {
//				for (InventoryItem inventoryItem : inventory.getItems()) {
//					if (inventoryItem.getName().equals(cartItem.getName())) {
//						inventory.removeItem(inventoryItem);
//						System.out.println("==== InventoryServiceDomainEventConsumer"
//							+ " - removed item from Inventory - " + inventoryItem);
//					}
//				}
//			}
//		}
//		
//	}

}
