package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class AddItemRequestDTO implements Serializable {

	private static final long serialVersionUID = -9008192694461656231L;

	private InventoryItem item;

	public AddItemRequestDTO() {
	}
	
	public AddItemRequestDTO(InventoryItem item) {
		this.item = item;
	}
	
	public InventoryItem getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "AddItemRequestDTO [item=" + item + "]";
	}
	
}
