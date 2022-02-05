package com.learning.ddd.onlinestore.cart.application.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.learning.ddd.onlinestore.cart.domain.CartItem;

public class AddItemToCartDTO implements Serializable {

	private static final long serialVersionUID = -4121629740413217523L;

	private String consumerId;
	private int cartId;
	private List<CartItem> items = new ArrayList<CartItem>();

	
	public AddItemToCartDTO(String consumerId, int cartId) {
		this.consumerId = consumerId;
		this.cartId = cartId;
	}


	public String getConsumerId() {
		return consumerId;
	}

	public int getCartId() {
		return cartId;
	}

	public List<CartItem> getItems() {
		return items;
	}


	public void addItem(CartItem item) {
		items.add(item);
	}

	
	@Override
	public String toString() {
		return "AddItemToCartDTO [consumerId=" + consumerId 
				+ ", items=" + items + "]";
	}
	
}
