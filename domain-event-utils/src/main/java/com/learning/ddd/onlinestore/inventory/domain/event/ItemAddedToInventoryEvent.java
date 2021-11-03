package com.learning.ddd.onlinestore.inventory.domain.event;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemAddedToInventoryEvent implements DomainEvent {

	private static final long serialVersionUID = -7827120941743495965L;
	
	private InventoryItem item;

	public ItemAddedToInventoryEvent(InventoryItem item) {
		this.item = item;
	}

	public InventoryItem getItem() {
		return item;
	}
	
	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ITEM_ADDED_TO_INVENTORY;
	}
	
	@Override
	public Object getEventData() {
		return item;
	}
	
	@Override
	public String toString() {
		return "ItemAddedToInventoryEvent [item=" + item + "]";
	}

}
