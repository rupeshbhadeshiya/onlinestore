package com.learning.ddd.onlinestore.inventory;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class DeleteItemsRequestDTO implements Serializable {

	private static final long serialVersionUID = 4252427210075623140L;

	private InventoryItem exampleItem;

	public DeleteItemsRequestDTO() {
	}

	public DeleteItemsRequestDTO(InventoryItem exampleItem) {
		this.exampleItem = exampleItem;
	}
	
	public InventoryItem getExampleItem() {
		return exampleItem;
	}

	@Override
	public String toString() {
		return "DeleteItemsRequestDTO [exampleItem=" + exampleItem + "]";
	}
	
}
