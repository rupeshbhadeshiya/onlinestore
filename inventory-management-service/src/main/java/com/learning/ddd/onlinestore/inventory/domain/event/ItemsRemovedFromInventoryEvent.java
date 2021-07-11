package com.learning.ddd.onlinestore.inventory.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemsRemovedFromInventoryEvent implements DomainEvent {

	private InventoryItem exampleItem; // Items were removed matching this exampleItem

	public ItemsRemovedFromInventoryEvent(InventoryItem exampleItem) {
		this.exampleItem = exampleItem;
	}
	
	public InventoryItem getexampleItem() {
		return exampleItem;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ITEMS_REMOVED_FROM_INVENTORY;
	}
	
	@Override
	public Object getEventData() {
		return exampleItem;
	}
	
	@Override
	public String toString() {
		return "ItemsRemovedFromInventoryEvent [exampleItem=" + exampleItem + "]";
	}
	
}
