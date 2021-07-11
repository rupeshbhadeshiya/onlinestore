package com.learning.ddd.onlinestore.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemTest {

	@Test
	void addQuantity() {
		InventoryItem item = new InventoryItem(1001, "Grocery", "Biscuit", "Parle-G", 2, 10.0);
		assertEquals(2, item.getQuantity());
		item.increaseQuantity(1);
		assertEquals(3, item.getQuantity());
	}
	
	@Test
	void removeQuantity() {
		InventoryItem item = new InventoryItem(1001, "Grocery", "Biscuit", "Parle-G", 2, 10.0);
		assertEquals(2, item.getQuantity());
		item.decreaseQuantity(1);
		assertEquals(1, item.getQuantity());
	}
	
}
