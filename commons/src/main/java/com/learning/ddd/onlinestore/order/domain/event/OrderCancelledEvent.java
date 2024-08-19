package com.learning.ddd.onlinestore.order.domain.event;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.order.application.dto.OrderInfo;

public class OrderCancelledEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = -1424868663082402450L;

	public OrderCancelledEvent(OrderInfo OrderInfo) {
		super(
			OnlinestoreDomainEventName.ORDER_CANCELLED,
			OrderInfo
		);
	}

	public OrderInfo getOrderInfo() {
		return (OrderInfo) getEventData();
	}
	
	@Override
	public String toString() {
		return "OrderCancelledEvent: OrderInfo = " + getOrderInfo();
	}
	
}