package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.shopping.domain.Cart;
import com.learning.ddd.onlinestore.shopping.domain.Item;

public class ItemRemovedFromCartEvent implements DomainEvent {

	private Cart cart;
	private Item item;

	public ItemRemovedFromCartEvent(Cart cart, Item item) {
		this.cart = cart;
		this.item = item;
	}

	public Cart getCart() {
		return cart;
	}
	
	public Item getItem() {
		return item;
	}
	
	@Override
	public DomainEventName getEventName() {
		return DomainEventName.ITEM_REMOVED_FROM_CART;
	}
	
	@Override
	public Object getEventData() {
		return new CartItemRemovedData(cart, item);
	}

	@Override
	public String toString() {
		return "ItemRemovedFromCartEvent [Cart=" + cart + ", item=" + item + "]";
	}
	
	public class CartItemRemovedData {
		
		private Item item;
		private Cart cart;
		
		public CartItemRemovedData(Cart cart, Item item) {
			super();
			this.item = item;
			this.cart = cart;
		}

		public Cart getCart() {
			return cart;
		}
		
		public Item getItem() {
			return item;
		}
		
		@Override
		public String toString() {
			return "CartItemRemovedData [item=" + item + ", cart=" + cart + "]";
		}
		
	}
	
}
