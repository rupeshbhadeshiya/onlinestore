package com.learning.ddd.onlinestore.cart.domain.exception;

public class CartNotFoundException extends Exception {

	private static final long serialVersionUID = -2394680231840198604L;
	
	private int cartId;
	
	public CartNotFoundException(int cartId) {
		this.cartId = cartId;
	}
	
	public int getCartId() {
		return cartId;
	}
	
}
