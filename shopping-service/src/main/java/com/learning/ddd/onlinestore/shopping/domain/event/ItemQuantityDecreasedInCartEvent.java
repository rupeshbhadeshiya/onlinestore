package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;

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
	public DomainEventName getEventName() {
		return DomainEventName.ITEM_QUANTITY_DECREASED_IN_CART;
	}
	
	@Override
	public Object getEventData() {
		return new CartItemQuantityDecreasedData(itemId, quantityToDecrease);
	}
	
	@Override
	public String toString() {
		return "ItemQuantityDecreasedInCartEvent [itemId=" + itemId 
				+ ", quantityToDecrease=" + quantityToDecrease + "]";
	}
	
	public class CartItemQuantityDecreasedData {
		
		private int itemId;
		private int quantityToDecrease;
		
		public CartItemQuantityDecreasedData(int itemId, int quantityToDecrease) {
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
			return "CartItemQuantityDecreasedData [itemId=" + itemId 
					+ ", quantityToDecrease=" + quantityToDecrease + "]";
		}
		
	}

}
