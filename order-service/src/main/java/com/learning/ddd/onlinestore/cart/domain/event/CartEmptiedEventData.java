package com.learning.ddd.onlinestore.cart.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.Cart;

public class CartEmptiedEventData implements Serializable {
	
	private static final long serialVersionUID = -8227416189834949327L;

	private Cart cart;

	public CartEmptiedEventData(Cart cart) {
		this.cart = cart;
	}

	public Cart getCart() {
		return cart;
	}

	@Override
	public String toString() {
		return "CartEmptiedEventData [cart=" + cart + "]";
	}
	
}
