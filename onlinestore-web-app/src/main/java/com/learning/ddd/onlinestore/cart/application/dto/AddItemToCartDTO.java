package com.learning.ddd.onlinestore.cart.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.CartItem;

public class AddItemToCartDTO implements Serializable {

	private static final long serialVersionUID = -4121629740413217523L;

	private String consumerId;
	private int cartId;
	private CartItem item;

	
	public AddItemToCartDTO(String consumerId, int cartId, CartItem cartItem) {
		this.consumerId = consumerId;
		this.cartId = cartId;
		item = cartItem;
	}


	public String getConsumerId() {
		return consumerId;
	}

	public int getCartId() {
		return cartId;
	}

	public CartItem getItem() {
		return item;
	}


	@Override
	public String toString() {
		return "AddItemToCartDTO [consumerId=" + consumerId + ", cartId=" + cartId + ", item=" + item + "]";
	}

}
