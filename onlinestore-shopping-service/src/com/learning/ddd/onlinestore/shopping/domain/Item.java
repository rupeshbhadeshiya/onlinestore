package com.learning.ddd.onlinestore.shopping.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//DDD: Entity
@Entity
public class Item {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	private String category;		// ex. Grocery
	private String subCategory;		// ex. Biscuits
	private String name;			// ex. Parle-G
	private int quantity;			// ex. 5
	private Double price;			// ex. 30.0 INR (price of a single item)
	
	@ManyToOne
	@JoinColumn(name="cartId", nullable=false)
	private Cart cart;				// Cart which contains this Item

	
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
	
	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + itemId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((subCategory == null) ? 0 : subCategory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (itemId != other.itemId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity != other.quantity)
			return false;
		if (subCategory == null) {
			if (other.subCategory != null)
				return false;
		} else if (!subCategory.equals(other.subCategory))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", category=" + category + ", subCategory=" + subCategory 
				+ ", name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
	}
	
}
