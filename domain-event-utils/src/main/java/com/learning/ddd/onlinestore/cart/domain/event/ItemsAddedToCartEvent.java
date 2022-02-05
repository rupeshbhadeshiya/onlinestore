package com.learning.ddd.onlinestore.cart.domain.event;

import java.io.Serializable;
import java.util.List;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class ItemsAddedToCartEvent implements DomainEvent {

	private static final long serialVersionUID = 4096923742584232002L;
	private ItemsAddedToCartEventData itemsAddedToCartEventData;

	public ItemsAddedToCartEvent(Cart cart, List<CartItem> items) {
		itemsAddedToCartEventData = new ItemsAddedToCartEventData(cart, items);
	}
	
	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ITEMS_ADDED_TO_CART;
	}

	@Override
	public Object getEventData() {
		return itemsAddedToCartEventData;
	}
	
	@Override
	public String toString() {
		return "ItemsAddedToCartEvent: " + itemsAddedToCartEventData;
	}
	
	public class ItemsAddedToCartEventData implements Serializable {
		
		private static final long serialVersionUID = 8645059401795086056L;
		
		private Cart cart;
		private List<CartItem> items;
		
		public ItemsAddedToCartEventData(Cart cart, List<CartItem> items) {
			super();
			this.cart = cart;
			this.items = items;
		}

		public Cart getCart() {
			return cart;
		}
		
		public List<CartItem> getItems() {
			return items;
		}

		@Override
		public String toString() {
			return "ItemsAddedToCartEventData [cart=" + cart + ", items=" + items + "]";
		}
		
	}
	
}
