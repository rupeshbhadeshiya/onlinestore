package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class UpdateItemResponseDTO implements Serializable {

	private static final long serialVersionUID = 3335638125489557344L;
	
	private InventoryItem item;
	
	public UpdateItemResponseDTO() {
	}

	public UpdateItemResponseDTO(InventoryItem item) {
		this.item = item;
	}

	public InventoryItem getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "UpdateItemResponseDTO [item=" + item + "]";
	}
	
}
