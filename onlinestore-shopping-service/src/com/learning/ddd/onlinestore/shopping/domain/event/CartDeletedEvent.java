package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.shopping.domain.Cart;

public class CartDeletedEvent implements DomainEvent {

	private Cart cart;

	public CartDeletedEvent(Cart cart) {
		this.cart = cart;
	}

	public Cart getCart() {
		return cart;
	}
	
}
