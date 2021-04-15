package com.learning.ddd.onlinestore.shopping.domain.exception;

public class ItemRemovalFromCartFailedException extends IndexOutOfBoundsException {

	private int itemId;
	private int quantityToRemove;

	public ItemRemovalFromCartFailedException(int itemId, int quantityToRemove) {
		super();
		this.itemId = itemId;
		this.quantityToRemove = quantityToRemove;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuantityToRemove() {
		return quantityToRemove;
	}

	public void setQuantityToRemove(int quantityToRemove) {
		this.quantityToRemove = quantityToRemove;
	}
	
}
