package com.learning.ddd.onlinestore.product.application.dto;

import java.util.List;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.product.domain.Product;

public class CheckProductsAvailabilityEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = 3291784828234328012L;
	
	private List<Product> products;	// this is eventData
	private int orderId;
	private boolean productsAvailableInInventory;

	// constructor to be used for request - where products availability is unknown 
	public CheckProductsAvailabilityEvent(int OrderId, List<Product> products, 
			OnlinestoreDomainEventName checkProductAvailabilityEventName) {
		super(
			checkProductAvailabilityEventName,
			products
		);
		this.orderId = OrderId;
		this.products = products;
	}

	// constructor to be used for response - where products availability is known
	public CheckProductsAvailabilityEvent(int OrderId, 
			List<Product> products, boolean productsAvailableInInventory, 
			OnlinestoreDomainEventName checkProductAvailabilityEventName) {
		super(
			checkProductAvailabilityEventName,
			products
		);
		this.orderId = OrderId;
		this.products = products;
		this.productsAvailableInInventory = productsAvailableInInventory;
	}

	public int getOrderId() {
		return orderId;
	}
	
	public List<Product> getProducts() {
		return products;
	}

	public boolean isProductsAvailableInInventory() {
		return productsAvailableInInventory;
	}

	@Override
	public String toString() {
		return "CheckProductsAvailabilityEvent ["
			+ "orderId=" + orderId
			+ ", products=" + products
			+ ", productsAvailableInInventory=" + productsAvailableInInventory + "]";
	}
	
}
