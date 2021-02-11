package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.inventory.domain.Item;

public class ItemRemovedFromCartEvent implements DomainEvent {

	private Item item;

	public ItemRemovedFromCartEvent(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "ItemRemovedFromCartEvent [item=" + item + "]";
	}
	
}
