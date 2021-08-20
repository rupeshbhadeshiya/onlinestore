package com.learning.ddd.onlinestore.inventory.domain.exception;

import java.util.List;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemsAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1031486610266197758L;

	private List<InventoryItem> items;

	public ItemsAlreadyExistsException(List<InventoryItem> items) {
		this.items = items;
	}

	public List<InventoryItem> getItems() {
		return items;
	}

}
