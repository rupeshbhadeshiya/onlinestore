package com.learning.ddd.onlinestore.inventory.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.Item;

public class ItemAddedToInventoryEvent implements DomainEvent {

	private Item item;

	public ItemAddedToInventoryEvent(Item item) {
		this.item = item;
	}

	public Item getItem() {
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
