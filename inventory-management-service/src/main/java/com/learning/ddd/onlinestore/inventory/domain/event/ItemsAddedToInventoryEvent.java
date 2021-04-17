package com.learning.ddd.onlinestore.inventory.domain.event;

import java.util.List;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.Item;

public class ItemsAddedToInventoryEvent implements DomainEvent {

	private List<Item> items;

	public ItemsAddedToInventoryEvent(List<Item> items) {
		this.items = items;
	}
	
	public List<Item> getItems() {
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