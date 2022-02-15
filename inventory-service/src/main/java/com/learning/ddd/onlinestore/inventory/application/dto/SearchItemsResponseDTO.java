package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;
import java.util.List;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class SearchItemsResponseDTO implements Serializable {

	private static final long serialVersionUID = 9123953032991053907L;

	private List<InventoryItem> items;

	public SearchItemsResponseDTO() {
	}

	public SearchItemsResponseDTO(List<InventoryItem> items) {
		this.items = items;
	}

	public List<InventoryItem> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "SearchItemsResponseDTO [items=" + items + "]";
	}
	
}
