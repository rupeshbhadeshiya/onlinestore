package com.learning.ddd.onlinestore.order.domain.event;

import java.util.List;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.order.domain.OrderItem;

public class OrderCancelledEvent implements DomainEvent {

	private static final long serialVersionUID = -3905939689385959486L;
	
	private List<OrderItem> orderItems;

	public OrderCancelledEvent(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ORDER_CANCELLED;
	}

	@Override
	public Object getEventData() {
		return orderItems;
	}

}
