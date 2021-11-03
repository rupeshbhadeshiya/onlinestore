package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

public class SearchInventoryItemDTO implements Serializable {

	private static final long serialVersionUID = -7988432318982096208L;

	private String category;		// ex. Grocery
	private String subCategory;		// ex. Biscuits
	private String name;			// ex. Parle-G
	private String quantity;		// ex. 5
	private String price;			// ex. 30.0 INR (price of a single item)

	
	public SearchInventoryItemDTO() {
	}
	

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}


	@Override
	public String toString() {
		return "SearchInventoryItemDTO [category=" + category 
				+ ", subCategory=" + subCategory + ", name=" + name
				+ ", quantity=" + quantity + ", price=" + price 
				+ "]";
	}


	public InventoryItem toInventoryItem() {
		
		InventoryItem item = new InventoryItem();
		
		item.setCategory(this.getCategory());
		item.setSubCategory(this.getSubCategory());
		item.setName(this.getName());
		try {
			item.setQuantity(Integer.parseInt(this.getQuantity()));
		} catch (NumberFormatException e) {
			item.setQuantity(-1);
		}
		try {
			item.setPrice(Double.parseDouble(this.getPrice()));
		} catch (NumberFormatException e) {
			item.setPrice(-1D);
		}
		
		return item;
	}
	
}
