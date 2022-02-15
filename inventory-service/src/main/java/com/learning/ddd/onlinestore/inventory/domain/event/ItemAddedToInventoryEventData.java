package com.learning.ddd.onlinestore.inventory.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemAddedToInventoryEventData implements Serializable {

	private static final long serialVersionUID = 7263056612507752622L;
	
	private InventoryItem item;

	public ItemAddedToInventoryEventData(InventoryItem item) {
		this.item = item;
	}

	public InventoryItem getItem() {
		return item;
	}
	
	@Override
	public String toString() {
		return "ItemAddedToInventoryEventData [item=" + item + "]";
	}

}
