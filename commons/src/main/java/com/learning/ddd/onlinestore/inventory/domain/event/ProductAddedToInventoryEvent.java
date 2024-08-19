package com.learning.ddd.onlinestore.inventory.domain.event;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.Product;

public class ProductAddedToInventoryEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = 1525660794604543221L;

	public ProductAddedToInventoryEvent(Product product) {
		super(
			OnlinestoreDomainEventName.PRODUCT_ADDED_TO_INVENTORY,
			product
		);
	}

	public Product getProduct() {
		return (Product) getEventData();
	}
	
	@Override
	public String toString() {
		return "ProductAddedToInventoryEvent: Product = " + getProduct();
	}
	
}
