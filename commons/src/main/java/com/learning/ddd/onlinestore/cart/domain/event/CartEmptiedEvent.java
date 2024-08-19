package com.learning.ddd.onlinestore.cart.domain.event;

import java.util.List;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.Product;

public class CartEmptiedEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = -7100438644033320457L;
	
	private List<Product> products;

	public CartEmptiedEvent(Integer cartId, List<Product> products) {
		super(
			OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER,
			cartId
		);
		this.products = products;
	}
	
	public CartEmptiedEvent(OnlinestoreDomainEventName eventName, Integer cartId, List<Product> products) {
		super(eventName, cartId);
		this.products = products;
	}

	public Integer getCartId() {
		return (Integer) getEventData();
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	@Override
	public String toString() {
		return "CartEmptiedEvent: CartId = " + getCartId() + ", products = " + getProducts();
	}
	
}
