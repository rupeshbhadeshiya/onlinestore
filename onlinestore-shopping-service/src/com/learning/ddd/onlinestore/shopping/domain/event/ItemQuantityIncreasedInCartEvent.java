package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;

public class ItemQuantityIncreasedInCartEvent implements DomainEvent {

	private int itemId;
	private int quantityToIncrease;

	public ItemQuantityIncreasedInCartEvent(int itemId, int quantityToIncrease) {
		this.itemId = itemId;
		this.quantityToIncrease = quantityToIncrease;
	}
	
	public int getItemId() {
		return itemId;
	}
	public int getQuantityToIncrease() {
		return quantityToIncrease;
	}

	@Override
	public String toString() {
		return "ItemQuantityIncreasedInCartEvent [itemId=" + itemId 
				+ ", quantityToIncrease=" + quantityToIncrease + "]";
	}
	
	

}
