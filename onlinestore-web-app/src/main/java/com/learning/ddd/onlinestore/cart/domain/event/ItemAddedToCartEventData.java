package com.learning.ddd.onlinestore.cart.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;

public class ItemAddedToCartEventData implements Serializable {

	private static final long serialVersionUID = 5404055313782552568L;

	private Cart cart;
	private CartItem item;

	public ItemAddedToCartEventData(Cart cart, CartItem item) {
		super();
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
	public String toString() {
		return "ItemAddedToCartEventData [cart=" + cart + ", item=" + item + "]";
	}

}
