package com.learning.ddd.onlinestore.cart.domain.exception;

public class ItemNotFoundInCartException extends RuntimeException {

	private static final long serialVersionUID = 8999580450650688758L;

	private int itemId;

	public ItemNotFoundInCartException(int itemId) {
		super();
		this.itemId = itemId;
	}
	
	public int getItemId() {
		return itemId;
	}

}
