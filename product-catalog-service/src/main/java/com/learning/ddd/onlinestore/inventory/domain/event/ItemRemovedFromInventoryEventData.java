package com.learning.ddd.onlinestore.inventory.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class ItemRemovedFromInventoryEventData implements Serializable {

	private static final long serialVersionUID = -5599348679593334455L;

	private InventoryItem item;

	public ItemRemovedFromInventoryEventData(InventoryItem item) {
		this.item = item;
	}
	
	public InventoryItem getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "ItemRemovedFromInventoryEventData [item=" + item + "]";
	}

}
