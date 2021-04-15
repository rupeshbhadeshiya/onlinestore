package com.learning.ddd.onlinestore.transaction.domain;

import java.util.List;

import com.learning.ddd.onlinestore.checkout.domain.OrderItem;

public class ItemInfo {

	private List<OrderItem> items;
	private int totalItems;
	private double totalAmount;
	
	public ItemInfo() {
	}

	public ItemInfo(List<OrderItem> items, int totalItems, double totalAmount) {
		super();
		this.items = items;
		this.totalItems = totalItems;
		this.totalAmount = totalAmount;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "ItemInfo [items=" + items + ", totalItems=" + totalItems + ", totalAmount=" + totalAmount + "]";
	}
	
}
