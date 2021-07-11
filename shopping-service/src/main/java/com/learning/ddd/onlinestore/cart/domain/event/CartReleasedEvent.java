package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.cart.domain.Cart;

public class CartReleasedEvent implements DomainEvent {

	private Cart cart;

	public CartReleasedEvent(Cart cart) {
		this.cart = cart;
	}
	
	public Cart getCart() {
		return cart;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.CART_RELEASED;
	}
	
	@Override
	public Object getEventData() {
		return cart;
	}
	
	@Override
	public String toString() {
		return "CartReleasedEvent [cart=" + cart + "]";
	}
	
}
