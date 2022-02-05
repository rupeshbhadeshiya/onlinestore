package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;
import java.util.List;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class GetItemsResponseDTO implements Serializable {

	private static final long serialVersionUID = 8317876536056286906L;

	private List<InventoryItem> items;

	private int itemsQuantitiesTotal;
	
	public GetItemsResponseDTO() {
	}

	public GetItemsResponseDTO(List<InventoryItem> items, int itemsQuantitiesTotal) {
		this.items = items;
		this.itemsQuantitiesTotal = itemsQuantitiesTotal;
	}
	
	public List<InventoryItem> getItems() {
		return items;
	}
	
	public int getItemsQuantitiesTotal() {
		return itemsQuantitiesTotal;
	}

	@Override
	public String toString() {
		return "GetItemsResponseDTO [items=" + items
				+ ", itemsQuantitiesTotal=" + itemsQuantitiesTotal + "]";
	}
	
	
}
