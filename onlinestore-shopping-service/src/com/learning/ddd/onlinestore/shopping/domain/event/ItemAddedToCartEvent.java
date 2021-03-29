package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.shopping.domain.Item;

public class ItemAddedToCartEvent implements DomainEvent {

	private Item item;

	public ItemAddedToCartEvent(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "ItemAddedToCartEvent [item=" + item + "]";
	}
	
}
