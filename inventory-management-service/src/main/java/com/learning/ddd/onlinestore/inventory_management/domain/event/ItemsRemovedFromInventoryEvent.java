package com.learning.ddd.onlinestore.inventory_management.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.inventory_management.domain.Item;

public class ItemsRemovedFromInventoryEvent implements DomainEvent {

	private Item exampleItem;

	public ItemsRemovedFromInventoryEvent(Item exampleItem) {
		this.exampleItem = exampleItem;
	}

	public Item getexampleItem() {
		return exampleItem;
	}

	@Override
	public String toString() {
		return "ItemsRemovedFromInventoryEvent [exampleItem=" + exampleItem + "]";
	}
	
}
