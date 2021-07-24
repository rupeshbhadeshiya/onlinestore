package com.learning.ddd.onlinestore.cart;

import java.util.ArrayList;
import java.util.List;

import com.learning.ddd.onlinestore.cart.domain.CartItem;

public class PullCartDTO {

	private String consumerId;
	private List<CartItem> items = new ArrayList<CartItem>();
	
	
	public PullCartDTO() {
	}

	public PullCartDTO(String consumerId) {
		this.consumerId = consumerId;
	}
	

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

	public void addItem(CartItem item) {
		items.add(item);
	}

	@Override
	public String toString() {
		return "PullCartDTO [consumerId=" + consumerId 
				+ ", items=" + items + "]";
	}
	
}
