package com.learning.ddd.onlinestore.inventory.application.dto;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class AddItemResponseDTO extends OnlinestoreBaseResponseDTO {

	private static final long serialVersionUID = 2230946385516013182L;

	private InventoryItem item;

	public AddItemResponseDTO() {
	}
	
	public AddItemResponseDTO(InventoryItem item) {
		this.item = item;
	}
	
	public InventoryItem getItem() {
		return item;
	}
	
	@Override
	public String toString() {
		return "AddItemResponseDTO [item=" + item + "]";
	}
	
}
