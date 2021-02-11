package com.learning.ddd.onlinestore.order.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.learning.ddd.onlinestore.order.domain.service.SearchOrderService;

public class SearchOrderServiceTest {

	@Test
	void searchOrders() {
		SearchOrderService searchOrderService = new SearchOrderService();
		List<Order> orders = searchOrderService.getAllOrders();
		assertNotNull(orders);
		assertEquals(1, orders.size());
	}

}
