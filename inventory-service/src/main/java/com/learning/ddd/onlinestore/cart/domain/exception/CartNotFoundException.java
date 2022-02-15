package com.learning.ddd.onlinestore.cart.domain.exception;

public class CartNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -472978848088879522L;
	private int cartId;

	public CartNotFoundException(int cartId) {
		super();
		this.cartId = cartId;
	}
	
	public int getCartId() {
		return cartId;
	}
	
}