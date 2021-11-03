package com.learning.ddd.onlinestore.cart.domain.exception;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;

public class CartItemNotFoundException extends Exception {

	private static final long serialVersionUID = 1537042592658848551L;
	
	private Cart cart;
	private CartItem cartItem;

	public CartItemNotFoundException(Cart cart, CartItem cartItem) {
		this.cart = cart;
		this.cartItem = cartItem;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public CartItem getCartItem() {
		return cartItem;
	}

}
