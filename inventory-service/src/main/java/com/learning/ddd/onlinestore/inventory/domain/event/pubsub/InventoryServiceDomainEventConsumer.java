package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.cart.domain.event.ItemRemovedFromCartEvent.ItemRemovedFromCartEventData;
import com.learning.ddd.onlinestore.cart.domain.event.ItemsAddedToCartEvent.ItemsAddedToCartEventData;
import com.learning.ddd.onlinestore.commons.util.ItemConversionUtil;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.exception.ItemAlreadyExistsException;
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
		
		if (domainEvent.getEventName().equals(DomainEventName.ITEMS_ADDED_TO_CART)) {
			
			ItemsAddedToCartEventData cartItemsAddedEventData = (ItemsAddedToCartEventData) domainEvent.getEventData();
			
			Cart cart = cartItemsAddedEventData.getCart();
		
			for (CartItem cartItem : cart.getItems()) {
				for (InventoryItem inventoryItem : inventory.getItems()) {
					if (inventoryItem.getName().equals(cartItem.getName())) {
						inventory.removeItem(inventoryItem);
						System.out.println("==== InventoryServiceDomainEventConsumer - "
								+ "Event: " + DomainEventName.ITEMS_ADDED_TO_CART.name()
								+ ", Removed corresponding item from Inventory - " + inventoryItem
						);
					}
				}
			}
		
		// [ Cases for which items needed to be ADDED back to Inventory ] //
		//	1. A Item is removed from a Cart
		//	2. A Cart is emptied
		//	3. An Order is cancelled		
		
		} else if (domainEvent.getEventName().equals(DomainEventName.ITEM_REMOVED_FROM_CART)) {
			
			ItemRemovedFromCartEventData itemRemovedFromCartEventData = (ItemRemovedFromCartEventData) domainEvent.getEventData();
			InventoryItem inventoryItem = ItemConversionUtil.fromCartItemToInventoryItem(itemRemovedFromCartEventData.getItem());
			
			try {
				inventory.addItem(inventoryItem);
			} catch (ItemAlreadyExistsException e) {
				System.err.println("==== InventoryServiceDomainEventConsumer - "
					+ "Event: " + DomainEventName.ITEM_REMOVED_FROM_CART.name()
					+ ", Failed adding corresponding item to Inventory - " + inventoryItem
				);
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			System.out.println("==== InventoryServiceDomainEventConsumer - "
				+ "Event: " + DomainEventName.ITEM_REMOVED_FROM_CART.name()
				+ ", Added corresponding item to Inventory - " + inventoryItem
			);
			
		} else if (domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED)) {
			
			CartEmptiedEvent cartEmptiedEvent = (CartEmptiedEvent) domainEvent;
			
			for (CartItem cartItem : cartEmptiedEvent.getCartItemsBeingReleased()) {
			
				InventoryItem inventoryItem = ItemConversionUtil.fromCartItemToInventoryItem(cartItem);
				
				try {
					inventory.addItem(inventoryItem);
				} catch (ItemAlreadyExistsException e) {
					System.err.println("==== InventoryServiceDomainEventConsumer - "
						+ "Event: " + DomainEventName.CART_EMPTIED.name()
						+ ", Failed adding corresponding item to Inventory - " + inventoryItem
					);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
				System.out.println("==== InventoryServiceDomainEventConsumer - "
					+ "Event: " + DomainEventName.CART_EMPTIED.name()
					+ ", Added corresponding item to Inventory - " + inventoryItem
				);
			}
			
		} else if (domainEvent.getEventName().equals(DomainEventName.ORDER_CANCELLED)) {
			
			OrderCancelledEvent orderCancelledEvent = (OrderCancelledEvent) domainEvent;
			
			for (OrderItem orderItem : orderCancelledEvent.getOrderItems()) {
				
				InventoryItem inventoryItem = ItemConversionUtil.fromOrderItemToInventoryItem(orderItem);
				
				try {
					inventory.addItem(inventoryItem);
				} catch (ItemAlreadyExistsException e) {
					System.err.println("==== InventoryServiceDomainEventConsumer - "
						+ "Event: " + DomainEventName.ORDER_CANCELLED.name()
						+ ", Failed adding corresponding item to Inventory - " + inventoryItem
					);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
				System.out.println("==== InventoryServiceDomainEventConsumer - "
					+ "Event: " + DomainEventName.ORDER_CANCELLED.name()
					+ ", Added corresponding item to Inventory - " + inventoryItem
				);
				
			}
		}
			
	}

}
