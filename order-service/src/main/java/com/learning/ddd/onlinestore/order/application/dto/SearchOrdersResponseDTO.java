package com.learning.ddd.onlinestore.order.application.dto;

import java.io.Serializable;
import java.util.List;

import com.learning.ddd.onlinestore.order.domain.Order;

public class SearchOrdersResponseDTO implements Serializable {

	private static final long serialVersionUID = 3316191215213543539L;
	
	private List<Order> orders;

	
	public SearchOrdersResponseDTO() {
	}

	public SearchOrdersResponseDTO(List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> getOrders() {
		return orders;
	}
	
	@Override
	public String toString() {
		return "SearchOrdersResponseDTO [orders=" + orders + "]";
	}

}
