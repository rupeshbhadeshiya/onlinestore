package com.learning.ddd.onlinestore.order.domain.event;

import com.learning.ddd.onlinestore.cart.domain.CartInfo;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.order.domain.OrderInfo;

public class OrderConfirmedEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = -7935109245427479506L;

	private CartInfo CartInfo;

	public OrderConfirmedEvent(OrderInfo order, CartInfo CartInfo) {
		super(
			OnlinestoreDomainEventName.ORDER_CONFIRMED,
			order
		);
		this.CartInfo = CartInfo;
	}

	public OrderInfo getOrderInfo() {
		return (OrderInfo) getEventData();
	}
	
	public CartInfo getCartInfo() {
		return CartInfo;
	}
	
	@Override
	public String toString() {
		return "OrderConfirmedEvent: OrderInfo = " + getOrderInfo() + ", CartInfo = " + getCartInfo();
	}
	
}
