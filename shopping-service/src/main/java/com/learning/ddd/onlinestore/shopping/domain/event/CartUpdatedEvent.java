package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.shopping.domain.Cart;

public class CartUpdatedEvent implements DomainEvent {

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
