package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.shopping.domain.Cart;
import com.learning.ddd.onlinestore.shopping.domain.Item;

public class ItemAddedToCartEvent implements DomainEvent {

	private Cart cart;
	private Item item;

	public ItemAddedToCartEvent(Cart cart, Item item) {
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
		
		private Item item;
		private Cart cart;
		
		public CartItemAddedData(Cart cart, Item item) {
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
			return "CartItemAddedData [item=" + item + ", cart=" + cart + "]";
		}
		
	}
	
}
