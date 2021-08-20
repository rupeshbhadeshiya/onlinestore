package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class CartUpdatedEvent implements DomainEvent {

	private static final long serialVersionUID = 3138507606094607916L;

	private Cart cart;

	public CartUpdatedEvent(Cart cart) {
		this.cart = cart;
	}
	
	public Cart getCart() {
		return cart;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.CART_UPDATED;
	}
	
	@Override
	public Object getEventData() {
		return cart;
	}
	
	@Override
	public String toString() {
		return "CartUpdatedEvent [cart=" + cart + "]";
	}
	
}
