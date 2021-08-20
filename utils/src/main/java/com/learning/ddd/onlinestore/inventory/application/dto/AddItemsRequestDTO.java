package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class AddItemsRequestDTO implements Serializable {

	private static final long serialVersionUID = -9008192694461656231L;

	@NotEmpty(message = "Specify one or more Items")
	private List<InventoryItem> items;

	public AddItemsRequestDTO() {
	}
	
	public AddItemsRequestDTO(List<InventoryItem> items) {
		this.items = items;
	}
	
	public List<InventoryItem> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "AddItemsRequestDTO [items=" + items + "]";
	}
	
}
