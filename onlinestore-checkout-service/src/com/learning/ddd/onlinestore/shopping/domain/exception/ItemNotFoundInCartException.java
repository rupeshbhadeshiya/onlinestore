package com.learning.ddd.onlinestore.shopping.domain.exception;

public class ItemNotFoundInCartException extends RuntimeException {

	private static final long serialVersionUID = 7299858934507806644L;
	
	private int itemId;

	public ItemNotFoundInCartException(int itemId) {
		super();
		this.itemId = itemId;
	}
	
	public int getItemId() {
		return itemId;
	}

}
