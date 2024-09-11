package com.learning.ddd.onlinestore.product.domain.event;

import java.util.List;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.product.domain.Product;

public class ProductsAddedToInventoryEvent  extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = -2588580495051699423L;

	public ProductsAddedToInventoryEvent(List<Product> products ) {
		super(
			OnlinestoreDomainEventName.PRODUCTS_ADDED_TO_INVENTORY,
			products
		);
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProducts() {
		return (List<Product>) getEventData();
	}
	
	@Override
	public String toString() {
		return "ProductsAddedToInventoryEvent: products = " + getProducts();
	}
	
}