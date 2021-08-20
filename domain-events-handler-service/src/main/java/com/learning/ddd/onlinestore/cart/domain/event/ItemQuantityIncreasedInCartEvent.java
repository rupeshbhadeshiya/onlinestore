package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class ItemQuantityIncreasedInCartEvent implements DomainEvent {

	private static final long serialVersionUID = -4728136253161384650L;
	
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
	public DomainEventName getEventName() {
		return DomainEventName.ITEM_QUANTITY_INCREASED_IN_CART;
	}
	
	@Override
	public Object getEventData() {
		return new CartItemQuantityIncreasedData(itemId, quantityToIncrease);
	}

	@Override
	public String toString() {
		return "ItemQuantityIncreasedInCartEvent [itemId=" + itemId 
				+ ", quantityToIncrease=" + quantityToIncrease + "]";
	}
	
	public class CartItemQuantityIncreasedData {
		
		private int itemId;
		private int quantityToIncrease;
		
		public CartItemQuantityIncreasedData(int itemId, int quantityToIncrease) {
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
			return "CartItemQuantityIncreasedData [itemId=" + itemId
					+ ", quantityToIncrease=" + quantityToIncrease + "]";
		}
		
	}

}
