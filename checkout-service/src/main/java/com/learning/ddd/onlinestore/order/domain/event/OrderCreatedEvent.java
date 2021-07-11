package com.learning.ddd.onlinestore.order.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.order.domain.Order;

public class OrderCreatedEvent implements DomainEvent {

	private Order order;

	public OrderCreatedEvent(Order order) {
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

	@Override
	public String toString() {
		return "OrderCreatedEvent [order=" + order + "]";
	}
	
}
