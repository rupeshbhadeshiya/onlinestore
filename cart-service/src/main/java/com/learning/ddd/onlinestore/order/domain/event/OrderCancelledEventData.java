package com.learning.ddd.onlinestore.order.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.order.domain.Order;

public class OrderCancelledEventData implements Serializable {
	
	private static final long serialVersionUID = 294191213509730916L;

	private Order order;

	public OrderCancelledEventData(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}
	
	@Override
	public String toString() {
		return "OrderCancelledEventData [order=" + order + "]";
	}

}
