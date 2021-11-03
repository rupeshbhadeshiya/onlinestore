package com.learning.ddd.onlinestore.inventory.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

//DDD: Entity
@Entity
//@Document(collection = "inventory")
public class InventoryItem implements Serializable {

	private static final long serialVersionUID = 2635836624711799146L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	
	@NotEmpty(message = "Specify Category")
	private String category;		// ex. Grocery
	
	@NotEmpty(message = "Specify Sub-Category")
	private String subCategory;		// ex. Biscuits
	
	@NotEmpty(message = "Specify Name")
	private String name;			// ex. Parle-G
	
	@Positive(message = "Specify a positive number for Quantity")
	private int quantity;			// ex. 5
	
	@Positive(message = "Specify a positive number for Price")
	private Double price;			// ex. 30.0 INR (price of a single item)

	
	public InventoryItem() {
	}
	
	public InventoryItem(String category, String subCategory, String name, int quantity, double price) {
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
		return "InventoryItem [itemId=" + itemId + ", category=" + category + ", subCategory=" + subCategory 
				+ ", name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		//result = prime * result + itemId; // necessary fields except id field
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
		InventoryItem other = (InventoryItem) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		//if (itemId != other.itemId)	// compare fields which truly represent
		//	return false;				// an InventoryItem, itemId is not that field
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
	
	
	
}