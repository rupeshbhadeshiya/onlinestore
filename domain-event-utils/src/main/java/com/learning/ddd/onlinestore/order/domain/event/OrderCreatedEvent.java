package com.learning.ddd.onlinestore.order.domain.event;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.order.domain.Order;

public class OrderCreatedEvent implements DomainEvent {

	private static final long serialVersionUID = -2679484643811135796L;

	private Cart cart;
	private Order order;

	public OrderCreatedEvent(Cart cart, Order order) {
		this.cart = cart;
		this.order = order;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ORDER_CREATED;
	}

	@Override
	public Object getEventData() {
		return order;
	}

	public Cart getCart() {
		return cart;
	}
	
	public Order getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return "OrderCreatedEvent [cart=" + cart + ", order=" + order + "]";
	}
	
}
