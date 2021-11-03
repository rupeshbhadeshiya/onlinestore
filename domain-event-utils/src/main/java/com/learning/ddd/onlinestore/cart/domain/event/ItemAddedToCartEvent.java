package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class ItemAddedToCartEvent implements DomainEvent {

	private static final long serialVersionUID = 4096923742584232002L;

	private Cart cart;
	private CartItem item;

	public ItemAddedToCartEvent(Cart cart, CartItem item) {
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
		return DomainEventName.ITEM_ADDED_TO_CART;
	}

	@Override
	public Object getEventData() {
		return new CartItemAddedData(cart, item);
	}
	
	@Override
	public String toString() {
		return "ItemAddedToCartEvent [Cart=" + cart + ", item=" + item + "]";
	}
	
	public class CartItemAddedData {
		
		private CartItem item;
		private Cart cart;
		
		public CartItemAddedData(Cart cart, CartItem item) {
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
			return "CartItemAddedData [item=" + item + ", cart=" + cart + "]";
		}
		
	}
	
}
