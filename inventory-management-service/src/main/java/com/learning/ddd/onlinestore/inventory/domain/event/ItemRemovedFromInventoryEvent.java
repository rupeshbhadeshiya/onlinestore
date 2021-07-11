package com.learning.ddd.onlinestore.inventory.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemRemovedFromInventoryEvent implements DomainEvent {

	private InventoryItem item;

	public ItemRemovedFromInventoryEvent(InventoryItem item) {
		this.item = item;
	}
	
	public InventoryItem getItem() {
		return item;
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ITEM_REMOVED_FROM_INVENTORY;
	}
	
	@Override
	public Object getEventData() {
		return item;
	}
	
	@Override
	public String toString() {
		return "ItemRemovedFromInventoryEvent [item=" + item + "]";
	}
	
}
