package com.learning.ddd.onlinestore.cart.domain.exception;

public class ItemRemovalFromCartFailedException extends IndexOutOfBoundsException {

	private static final long serialVersionUID = 2128414722189782451L;

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
