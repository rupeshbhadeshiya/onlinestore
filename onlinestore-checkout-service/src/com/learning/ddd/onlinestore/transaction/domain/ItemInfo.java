package com.learning.ddd.onlinestore.transaction.domain;

import java.util.List;

import com.learning.ddd.onlinestore.inventory.domain.Item;

public class ItemInfo {

	private List<Item> items;
	private int totalItems;
	private double totalAmount;
	
	public ItemInfo() {
	}

	public ItemInfo(List<Item> items, int totalItems, double totalAmount) {
		super();
		this.items = items;
		this.totalItems = totalItems;
		this.totalAmount = totalAmount;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
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
