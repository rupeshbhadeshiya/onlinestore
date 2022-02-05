package com.learning.ddd.onlinestore.cart.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class ItemRemovedFromCartEvent implements DomainEvent {

	private static final long serialVersionUID = -1515002142339652818L;

	private ItemRemovedFromCartEventData itemRemovedFromCartEventData;

	public ItemRemovedFromCartEvent(Cart cart, CartItem item) {
		this.itemRemovedFromCartEventData = new ItemRemovedFromCartEventData(cart, item);
	}

	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ITEM_REMOVED_FROM_CART;
	}
	
	@Override
	public Object getEventData() {
		return this.itemRemovedFromCartEventData;
	}

	@Override
	public String toString() {
		return "ItemRemovedFromCartEvent: " + itemRemovedFromCartEventData;
	}
	
	public class ItemRemovedFromCartEventData implements Serializable {
		
		private static final long serialVersionUID = 6572563233813619528L;

		private Cart cart;
		private CartItem item;
		
		public ItemRemovedFromCartEventData(Cart cart, CartItem item) {
			super();
			this.item = item;
			this.cart = cart;
		}

		public Cart getCart() {
			return cart;
		}
		
		public CartItem getItem() {
			return item;
		}

		@Override
		public String toString() {
			return "ItemRemovedFromCartEventData [cart=" + cart + ", itemRemoved=" + item + "]";
		}
		
	}
	
}
