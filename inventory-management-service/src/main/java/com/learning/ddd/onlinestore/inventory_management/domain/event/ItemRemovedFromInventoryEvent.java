package com.learning.ddd.onlinestore.inventory_management.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.inventory_management.domain.Item;

public class ItemRemovedFromInventoryEvent implements DomainEvent {

	private Item item;

	public ItemRemovedFromInventoryEvent(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "ItemRemovedFromInventoryEvent [item=" + item + "]";
	}
	
}
