package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;
import java.util.List;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class AddItemsResponseDTO implements Serializable {

	private static final long serialVersionUID = 2230946385516013182L;

	private List<InventoryItem> items;
	private int itemsQuantitiesTotal;

	public AddItemsResponseDTO() {
	}
	
	public AddItemsResponseDTO(List<InventoryItem> items, int itemsQuantitiesTotal) {
		this.items = items;
		this.itemsQuantitiesTotal = itemsQuantitiesTotal;
	}

	public List<InventoryItem> getItems() {
		return items;
	}
	
	public void setItems(List<InventoryItem> items) {
		this.items = items;
	}

	public int getItemsQuantitiesTotal() {
		return itemsQuantitiesTotal;
	}
	
	public void setItemsQuantitiesTotal(int itemsQuantitiesTotal) {
		this.itemsQuantitiesTotal = itemsQuantitiesTotal;
	}

	@Override
	public String toString() {
		return "AddItemsResponseDTO [items=" + items 
				+ ", itemsQuantitiesTotal=" + itemsQuantitiesTotal 
				+ "]";
	}
	
}
