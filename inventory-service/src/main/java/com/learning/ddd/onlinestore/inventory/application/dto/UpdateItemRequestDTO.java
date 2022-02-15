package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class UpdateItemRequestDTO implements Serializable {

	private static final long serialVersionUID = -4780747589475571570L;

	private InventoryItem item;
	
	public UpdateItemRequestDTO() {
	}

	public UpdateItemRequestDTO(InventoryItem item) {
		this.item = item;
	}

	public InventoryItem getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "UpdateItemRequestDTO [item=" + item + "]";
	}
	
}
