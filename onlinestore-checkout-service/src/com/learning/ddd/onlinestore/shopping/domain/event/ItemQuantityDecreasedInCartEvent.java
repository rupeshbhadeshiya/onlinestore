package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;

public class ItemQuantityDecreasedInCartEvent implements DomainEvent {

	private int itemId;
	private int quantityToDecrease;

	public ItemQuantityDecreasedInCartEvent(int itemId, int quantityToDecrease) {
		this.itemId = itemId;
		this.quantityToDecrease = quantityToDecrease;
	}
	
	public int getItemId() {
		return itemId;
	}
	public int getQuantityToDecrease() {
		return quantityToDecrease;
	}

	@Override
	public String toString() {
		return "ItemQuantityDecreasedInCartEvent [itemId=" + itemId 
				+ ", quantityToDecrease=" + quantityToDecrease + "]";
	}
	
	

}
