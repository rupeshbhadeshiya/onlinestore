package com.learning.ddd.onlinestore.cart.domain.exception;

public class ItemNotFoundInCartException extends RuntimeException {

	private int itemId;

	public ItemNotFoundInCartException(int itemId) {
		super();
		this.itemId = itemId;
	}
	
	public int getItemId() {
		return itemId;
	}

}
