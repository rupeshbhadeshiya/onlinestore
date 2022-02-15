package com.learning.ddd.onlinestore.cart.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;

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
