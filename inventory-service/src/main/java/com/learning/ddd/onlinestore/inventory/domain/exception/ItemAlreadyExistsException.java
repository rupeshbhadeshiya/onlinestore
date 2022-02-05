package com.learning.ddd.onlinestore.inventory.domain.exception;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -2483135246410772119L;

	private InventoryItem item;

	public ItemAlreadyExistsException(InventoryItem item) {
		this.item = item;
	}

	public InventoryItem getItem() {
		return item;
	}

}
