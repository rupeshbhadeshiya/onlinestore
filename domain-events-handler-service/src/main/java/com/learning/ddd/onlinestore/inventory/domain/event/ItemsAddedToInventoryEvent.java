package com.learning.ddd.onlinestore.inventory.domain.event;

import java.util.List;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemsAddedToInventoryEvent implements DomainEvent {

	private static final long serialVersionUID = -4836966880294795945L;

	private List<InventoryItem> items;

	public ItemsAddedToInventoryEvent(List<InventoryItem> items) {
		this.items = items;
	}
	
	public List<InventoryItem> getItems() {
		return items;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ITEMS_ADDED_TO_INVENTORY;
	}
	
	@Override
	public Object getEventData() {
		return items;
	}
	
	@Override
	public String toString() {
		return "ItemsAddedToInventoryEvent [items=" + items + "]";
	}

}
