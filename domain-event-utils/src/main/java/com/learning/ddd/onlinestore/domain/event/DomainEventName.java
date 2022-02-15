package com.learning.ddd.onlinestore.domain.event;

public enum DomainEventName {
	
	// Inventory
	ITEM_ADDED_TO_INVENTORY, 
	ITEM_REMOVED_FROM_INVENTORY, 
	
	// Cart (Shopping)
	ITEM_ADDED_TO_CART, 
	ITEM_REMOVED_FROM_CART, 
	CART_EMPTIED_DUE_TO_ORDER_CREATION, 
	CART_EMPTIED_BY_CONSUMER, 
	
	// Order (Checkout)
	ORDER_CREATED, 
	ORDER_CANCELLED

}
