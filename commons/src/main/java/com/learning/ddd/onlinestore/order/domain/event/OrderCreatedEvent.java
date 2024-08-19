package com.learning.ddd.onlinestore.order.domain.event;

import com.learning.ddd.onlinestore.cart.application.dto.CartInfo;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.order.application.dto.OrderInfo;

public class OrderCreatedEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = -7935109245427479506L;

	private CartInfo CartInfo;

	public OrderCreatedEvent(OrderInfo order, CartInfo CartInfo) {
		super(
			OnlinestoreDomainEventName.ORDER_CREATED,
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
		return "OrderCreatedEvent: OrderInfo = " + getOrderInfo() + ", CartInfo = " + getCartInfo();
	}
	
}
