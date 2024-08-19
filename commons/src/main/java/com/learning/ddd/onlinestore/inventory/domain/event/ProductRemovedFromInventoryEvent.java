package com.learning.ddd.onlinestore.inventory.domain.event;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.Product;

public class ProductRemovedFromInventoryEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = 6063881881754512162L;

	public ProductRemovedFromInventoryEvent(Product product) {
		super(
			OnlinestoreDomainEventName.PRODUCT_REMOVED_FROM_INVENTORY, 
			product
		);
	}

	public Product getProduct() {
		return (Product) getEventData();
	}
	
	@Override
	public String toString() {
		return "ProductRemovedFromInventoryEvent: Product = " + getProduct();
	}
	
}
