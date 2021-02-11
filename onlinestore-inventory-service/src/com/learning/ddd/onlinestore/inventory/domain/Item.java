package com.learning.ddd.onlinestore.inventory.domain;

//DDD: Entity
public class Item {

	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	private String category;		// ex. Grocery
	private String subCategory;		// ex. Biscuits
	private String name;			// ex. Parle-G
	private int quantity;			// ex. 5
	private Double price;			// ex. 30.0 INR (price of a single item)

	
	public Item() {
	}
	
	public Item(int itemId, String category, String subCategory, String name, int quantity, double price) {
		this.itemId = itemId;
		this.category = category;
		this.subCategory = subCategory;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}

	
	public void increaseQuantity(int quantityToIncrease) {
		this.quantity += quantityToIncrease;
		
	}
	public void decreaseQuantity(int quantityToDecrease) {
		this.quantity -= quantityToDecrease;
	}
	
	
	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", category=" + category + ", subCategory=" + subCategory 
				+ ", name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
	}


	public Object compareTo(Item exampleItem) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
