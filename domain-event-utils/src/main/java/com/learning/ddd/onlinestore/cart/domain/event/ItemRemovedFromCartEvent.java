package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class ItemRemovedFromCartEvent implements DomainEvent {

	private static final long serialVersionUID = -1515002142339652818L;

	private Cart cart;
	private CartItem item;

	public ItemRemovedFromCartEvent(Cart cart, CartItem item) {
		this.cart = cart;
		this.item = item;
	}

	public Cart getCart() {
		return cart;
	}
	
	public CartItem getItem() {
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
		
		private CartItem item;
		private Cart cart;
		
		public CartItemRemovedData(Cart cart, CartItem item) {
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
			return "CartItemRemovedData [item=" + item + ", cart=" + cart + "]";
		}
		
	}
	
}
