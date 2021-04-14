package com.learning.ddd.onlinestore.inventory_management.domain.event;

import java.util.List;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.inventory_management.domain.Item;

public class ItemsAddedToInventoryEvent implements DomainEvent {

	private List<Item> items;

	public ItemsAddedToInventoryEvent(List<Item> items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "ItemsAddedToInventoryEvent [items=" + items + "]";
	}
	
}
