package com.learning.ddd.onlinestore.inventory.domain.exception;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemNotAvailableForShoppingException extends Exception {

	private static final long serialVersionUID = 6241955422877462550L;

	private InventoryItem item;

	public ItemNotAvailableForShoppingException(InventoryItem item) {
		this.item = item;
	}

	public InventoryItem getItem() {
		return item;
	}

}
