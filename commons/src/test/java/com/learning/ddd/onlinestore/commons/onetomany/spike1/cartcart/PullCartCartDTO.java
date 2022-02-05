package com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PullCartCartDTO implements Serializable {

	private static final long serialVersionUID = -4896494726577898715L;
	
	private String consumerId;
	private List<CartCartItem> items = new ArrayList<CartCartItem>();
	
	
	public PullCartCartDTO() {
	}

	public PullCartCartDTO(String consumerId) {
		this.consumerId = consumerId;
	}
	

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public List<CartCartItem> getItems() {
		return items;
	}

	public void setItems(List<CartCartItem> items) {
		this.items = items;
	}

	public void addItem(CartCartItem item) {
		items.add(item);
	}

	@Override
	public String toString() {
		return "PullCartDTO [consumerId=" + consumerId 
				+ ", items=" + items + "]";
	}
	
}
