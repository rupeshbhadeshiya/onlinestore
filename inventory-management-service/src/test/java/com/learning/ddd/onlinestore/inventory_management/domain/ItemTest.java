package com.learning.ddd.onlinestore.inventory_management.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ItemTest {

	@Test
	void addQuantity() {
		Item item = new Item(1001, "Grocery", "Biscuit", "Parle-G", 2, 10.0);
		assertEquals(2, item.getQuantity());
		item.increaseQuantity(1);
		assertEquals(3, item.getQuantity());
	}
	
	@Test
	void removeQuantity() {
		Item item = new Item(1001, "Grocery", "Biscuit", "Parle-G", 2, 10.0);
		assertEquals(2, item.getQuantity());
		item.decreaseQuantity(1);
		assertEquals(1, item.getQuantity());
	}
	
}
