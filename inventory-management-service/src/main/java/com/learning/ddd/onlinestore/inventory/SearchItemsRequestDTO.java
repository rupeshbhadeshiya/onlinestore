package com.learning.ddd.onlinestore.inventory;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class SearchItemsRequestDTO implements Serializable {
	
	private static final long serialVersionUID = -534406552637744619L;

	private InventoryItem exampleItem;

	public SearchItemsRequestDTO() {
	}

	public SearchItemsRequestDTO(InventoryItem exampleItem) {
		this.exampleItem = exampleItem;
	}
	
	public InventoryItem getExampleItem() {
		return exampleItem;
	}

	@Override
	public String toString() {
		return "SearchItemsRequestDTO [exampleItem=" + exampleItem + "]";
	}
	
}
