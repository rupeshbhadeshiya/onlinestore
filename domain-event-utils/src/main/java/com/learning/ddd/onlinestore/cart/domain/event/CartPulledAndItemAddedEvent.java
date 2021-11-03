package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class CartPulledAndItemAddedEvent implements DomainEvent {

	private static final long serialVersionUID = -12347715948908283L;

	private Cart cart;

	public CartPulledAndItemAddedEvent(Cart cart) {
		this.cart = cart;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.CART_PULLED;
	}

	@Override
	public Object getEventData() {
		return cart;
	}

	@Override
	public String toString() {
		return "CartPulled(Created)AndItemAddedEvent [cart=" + cart + "]";
	}
	
}
