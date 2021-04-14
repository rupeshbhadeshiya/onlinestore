package com.learning.ddd.onlinestore.shopping.domain.event;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.shopping.domain.Cart;
import com.learning.ddd.onlinestore.shopping.domain.Item;

public class ItemRemovedFromCartEvent implements DomainEvent {

	private Item item;
	private Cart cart;

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
	public String toString() {
		return "ItemRemovedFromCartEvent [Cart=" + cart + ", item=" + item + "]";
	}
	
}
