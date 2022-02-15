package com.learning.ddd.onlinestore.order.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.order.domain.Order;

public class OrderCreatedEventData implements Serializable {
	
	private static final long serialVersionUID = 8603664616160718559L;

	private Order order;
	private Cart cart;
	
	public OrderCreatedEventData(Order order, Cart cart) {
		this.order = order;
		this.cart = cart;
	}

	public Cart getCart() {
		return cart;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return "OrderCreatedEventData [order=" + order + ", cart=" + cart + "]";
	}

}
